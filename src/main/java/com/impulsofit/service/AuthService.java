package com.impulsofit.service;

import com.impulsofit.dto.request.*;
import com.impulsofit.dto.response.AuthResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import com.impulsofit.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final int MAX_INTENTOS = 5;
    private static final long MINUTOS_DESBLOQUEO = 60;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PerfilRepository perfilRepository;
    private final PublicacionRepository publicacionRepository;
    private final ComentarioRepository comentarioRepository;
    private final RetoRepository retoRepository;
    private final ParticipacionRetoRepository participacionRetoRepository;
    private final RegistroProcesoRepository registroProcesoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final GrupoRepository grupoRepository;
    private final SeguidoRepository seguidoRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO req) {
        //Validacion de correo
        if (usuarioRepository.existsByEmailIgnoreCase(req.email())) {
            throw new AlreadyExistsException("Ya existe un usuario con el correo: " + req.email());
        }

        validarDateyGender(req);

        //Crear Usuario con credenciales
        Usuario usuarioEntity = new Usuario();
        usuarioEntity.setEmail(req.email().toLowerCase());
        usuarioEntity.setContrasena(passwordEncoder.encode(req.contrasena()));
        usuarioEntity.setCodPregunta(req.cod_pregunta());
        usuarioEntity.setRespuesta(req.respuesta());

        Role userRole = roleRepository.findByNombre(RoleType.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role ROLE_USER not found"));
        usuarioEntity.setRole(userRole);

        Usuario saved = usuarioRepository.save(usuarioEntity);

        //Crear Persona asociada al Usuario
        Persona personaEntity = new Persona();
        personaEntity.setNombres(req.nombres());
        personaEntity.setApellidoP(req.apellido_p());
        personaEntity.setApellidoM(req.apellido_m());
        personaEntity.setFechaNacimiento(req.fecha_nacimiento());
        personaEntity.setGenero(req.genero());
        personaEntity.setUsuario(usuarioEntity);

        personaRepository.save(personaEntity);

        // Generar JWT con email, nombre y customerId
        String token = jwtUtil.generateToken(
                saved.getEmail(),
                personaEntity.getNombres(),
                personaEntity.getIdPersona().toString()
        );

        return new AuthResponseDTO(token, usuarioEntity.getEmail(), personaEntity.getNombres());
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginDTO) {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(loginDTO.email())
                .orElseThrow(() -> new BusinessRuleException("Usuario no encontrado"));

        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessRuleException("Persona no encontrada"));

        //Verificar si el usuario está bloqueado y si corresponde desbloquearlo
        if (Boolean.TRUE.equals(usuario.getBloqueado())) {
            LocalDateTime fechaBloqueo = usuario.getFechaBloqueo();
            if (fechaBloqueo != null) {
                LocalDateTime ahora = LocalDateTime.now();
                if (Duration.between(fechaBloqueo, ahora).toMinutes() >= MINUTOS_DESBLOQUEO) {
                    //Se cumplió el tiempo de bloqueo -> desbloqueamos y reseteamos intentos
                    usuario.setBloqueado(false);
                    usuario.setIntentosFallidos(0);
                    usuario.setFechaBloqueo(null);
                    usuarioRepository.save(usuario);
                } else {
                    throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
                }
            } else {
                throw new BusinessRuleException("Usuario bloqueado por múltiples intentos fallidos");
            }
        }

        // Autenticar con Spring Security y actualizar contador de intentos
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.email(),
                            loginDTO.contrasena()
                    )
            );
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            // Credenciales incorrectas: aumentar intentos
            int intentos = (usuario.getIntentosFallidos() == null)
                    ? 1
                    : usuario.getIntentosFallidos() + 1;

            boolean bloquear = false;
            if (intentos > MAX_INTENTOS) {      // en el 6to intento
                bloquear = true;
            }

            persistirIntentosYBloqueo(usuario.getIdUsuario(), intentos, bloquear);
            throw new BusinessRuleException("Credenciales incorrectas");
        } catch (org.springframework.security.core.AuthenticationException ex) {
            // Otras razones de fallo (cuenta deshabilitada, etc.)
            throw new BusinessRuleException("No se pudo autenticar al usuario");
        }

        // Login exitoso: resetear intentos y bloqueo
        persistirIntentosYBloqueo(usuario.getIdUsuario(), 0, false);

        // Generar JWT con email, nombres e idPersona
        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                persona.getNombres(),
                persona.getIdPersona().toString()
        );

        return new AuthResponseDTO(token, usuario.getEmail(), persona.getNombres());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persistirIntentosYBloqueo(Long idusuario, int intentos, boolean bloquear) {
        Usuario u = usuarioRepository.findById(idusuario).orElse(null);
        if (u == null) return;
        u.setIntentosFallidos(intentos);
        if (bloquear) u.setBloqueado(true);
        if (bloquear) u.setFechaBloqueo(LocalDateTime.now());
        if (!bloquear) u.setFechaBloqueo(null);
        usuarioRepository.save(u);
    }

    @Transactional
    public AuthResponseDTO recoverCred(CredentialsRequestDTO req) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario registrado con el email: " + req.email()
                ));

        // Validar respuesta secreta
        String respIngresada = req.respuesta();
        String respGuardada = usuario.getRespuesta();

        if (respIngresada == null || respIngresada.trim().isEmpty()
                || respGuardada == null
                || !respIngresada.trim().equalsIgnoreCase(respGuardada.trim())) {
            throw new BusinessRuleException("La respuesta es incorrecta.");
        }

        // Validar nueva contraseña
        String newPass = req.new_contrasena();
        if (newPass == null || newPass.isBlank()) {
            throw new BusinessRuleException("La nueva contraseña no puede estar vacía.");
        }

        // Actualizar contraseña y resetear bloqueo / intentos
        usuario.setContrasena(newPass);
        usuario.setIntentosFallidos(0);
        usuario.setBloqueado(false);
        usuario.setFechaBloqueo(null);

        usuarioRepository.save(usuario);

        // Obtener Persona y generar nuevo JWT
        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessRuleException("Persona no encontrada"));

        String token = jwtUtil.generateToken(
                usuario.getEmail(),
                persona.getNombres(),
                persona.getIdPersona().toString()
        );

        return new AuthResponseDTO(token, usuario.getEmail(), persona.getNombres());
    }

    @Transactional
    public void delete(DeleteRequestDTO req) {
        if (!req.confirm()) throw new BusinessRuleException("Confirmación requerida para eliminar cuenta");

        Usuario usuarioEntity = usuarioRepository.findByEmailIgnoreCase(req.email())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario registrado con el email:  "
                        + req.email() ));

        if (req.constrasena() == null || !req.constrasena().equals(usuarioEntity.getContrasena())) {
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        Persona persona = personaRepository.findByUsuario(usuarioEntity)
                .orElseThrow(() -> new BusinessRuleException("Persona no encontrada"));

        // Obtener perfiles asociados a la persona
        List<Perfil> perfiles = perfilRepository.findAllByPersona_IdPersona(persona.getIdPersona());
        List<Long> perfilIds = perfiles.stream().map(Perfil::getIdPerfil).collect(Collectors.toList());

        // Comprobar si alguno de estos perfiles es creador de grupos
        List<Grupo> gruposCreados = grupoRepository.findAll().stream()
                .filter(g -> g.getPerfilCreador() != null && perfilIds.contains(g.getPerfilCreador().getIdPerfil()))
                .collect(Collectors.toList());

        if (!gruposCreados.isEmpty()) {
            throw new BusinessRuleException("No puedes borrar tu cuenta ya que eres el creador de " + gruposCreados.size() + " grupos");
        }

        // Borrar publicaciones y sus comentarios
        for (Long pid : perfilIds) {
            List<Publicacion> pubs = publicacionRepository.findAllByPerfil_IdPerfil(pid);
            for (Publicacion p : pubs) {
                List<Comentario> comentarios = comentarioRepository.findByPublicacion_IdPublicacion(p.getIdPublicacion());
                comentarioRepository.deleteAll(comentarios);
            }
            publicacionRepository.deleteAll(pubs);
        }

        // Borrar retos creados y sus participaciones y registros
        for (Long pid : perfilIds) {
            List<Reto> retos = retoRepository.findAllByPerfilCreador_IdPerfil(pid);
            for (Reto r : retos) {
                // eliminar registros asociados a participaciones del reto
                List<RegistroProceso> registros = registroProcesoRepository.findByParticipacionReto_Reto(r);
                registroProcesoRepository.deleteAll(registros);

                // eliminar participaciones en el reto
                List<ParticipacionReto> parts = participacionRetoRepository.findAll();
                List<ParticipacionReto> paraEliminar = parts.stream()
                        .filter(pr -> pr.getIdReto().equals(r.getIdReto()))
                        .collect(Collectors.toList());
                participacionRetoRepository.deleteAll(paraEliminar);
            }
            retoRepository.deleteAll(retos);
        }

        // Borrar membresias de grupo del usuario
        for (Long pid : perfilIds) {
            List<MembresiaGrupo> membresias = membresiaGrupoRepository.findAll().stream()
                    .filter(m -> m.getPerfil() != null && m.getPerfil().getIdPerfil().equals(pid))
                    .collect(Collectors.toList());
            membresiaGrupoRepository.deleteAll(membresias);
        }

        // Borrar seguidos y seguidores relacionados con los perfiles
        for (Long pid : perfilIds) {
            // borrar registros donde sea seguidor
            List<Seguido> seguidos = seguidoRepository.findAllById_IdSeguidorOrderByFechaSeguidoDesc(pid);
            seguidoRepository.deleteAll(seguidos);
            // borrar registros donde sea seguido
            List<Seguido> seguidores = seguidoRepository.findAllById_IdSeguidoOrderByFechaSeguidoDesc(pid);
            seguidoRepository.deleteAll(seguidores);
        }

        // Borrar participaciones propias en otros retos y registros asociados
        for (Long pid : perfilIds) {
            List<ParticipacionReto> participaciones = participacionRetoRepository.findAllByIdPerfil(pid);
            for (ParticipacionReto pr : participaciones) {
                List<RegistroProceso> regs = registroProcesoRepository.findByParticipacionRetoOrderByFechaDesc(pr);
                registroProcesoRepository.deleteAll(regs);
            }
            participacionRetoRepository.deleteAll(participaciones);
        }

        //Borrar perfiles, persona y usuario
        perfilRepository.deleteAll(perfiles);
        personaRepository.delete(persona);
        usuarioRepository.deleteById(usuarioEntity.getId());
    }

    private void validarDateyGender(RegisterRequestDTO u) {
        //Fecha no puede estar vacía
        if (u.fecha_nacimiento() == null) {
            throw new BusinessRuleException("La fecha de nacimiento no puede estar vacía.");
        }
        //Fecha no puede ser del futuro
        if (u.fecha_nacimiento().isAfter(LocalDate.now())) {
            throw new BusinessRuleException(
                    "La fecha de nacimiento no puede ser una fecha futura. " +
                            "Fecha ingresada: " + u.fecha_nacimiento()
            );
        }
        //Usuario debe tener mínimo 15 años de edad
        int edad = Period.between(u.fecha_nacimiento(), LocalDate.now()).getYears();
        if (edad < 15) {
            throw new BusinessRuleException(
                    "El usuario debe tener al menos 15 años. Edad actual: " + edad
            );
        }
        //Genero no puede estar vacío
        if (u.genero() == null) {
            throw new BusinessRuleException("Genero no puede estar vacío");
        }
        //Usuario no puede ser diferente de "M" o "F"
        if (!u.genero().equals("M") && !u.genero().equals("F")) {
            throw new BusinessRuleException("Formato de género inválido. Solo se permite: M o F");
        }
    }
}