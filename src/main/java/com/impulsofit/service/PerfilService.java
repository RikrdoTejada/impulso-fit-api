package com.impulsofit.service;

import com.impulsofit.dto.request.CredentialsRequestDTO;
import com.impulsofit.dto.request.PerfilRequestDTO;
import com.impulsofit.dto.response.AuthResponseDTO;
import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Persona;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.PersonaRepository;
import com.impulsofit.repository.UsuarioRepository;
import com.impulsofit.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public PerfilResponseDTO crearPerfil(PerfilRequestDTO req) {
        if(perfilRepository.existsByNombrePerfilIgnoreCase((req.nombre_perfil()))) {
            throw new AlreadyExistsException("Ya existe un perfil con el nombre: " + req.nombre_perfil());
        }

        // Obtener email del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessRuleException("Usuario no autenticado");
        }

        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails ud) {
            email = ud.getUsername();
        } else {
            email = authentication.getName();
        }

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario registrado con el email: " + email));

        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new BusinessRuleException("Persona no encontrada para el usuario: " + email));

        // Una sola instancia de Perfil por Persona
        List<Perfil> existentes = perfilRepository.findAllByPersona_IdPersona(persona.getIdPersona());
        if (existentes != null && !existentes.isEmpty()) {
            throw new AlreadyExistsException("Ya existe un perfil asociado a esta persona");
        }

        Perfil perfilEntity = new Perfil();
        perfilEntity.setNombrePerfil(req.nombre_perfil());
        perfilEntity.setBiografia(req.biografia());
        perfilEntity.setPersona(persona);
        perfilEntity.setUbicacion(req.ubicacion());
        perfilEntity.setFotoPerfil(req.foto_perfil());Perfil saved = perfilRepository.save(perfilEntity);

        return mapToResponse(saved);
    }

    @Transactional
    public PerfilResponseDTO actualizarPerfil(Long idPerfil, PerfilRequestDTO request) {
        Perfil perfil = perfilRepository.findById(idPerfil)
                .orElseThrow(() -> new BusinessRuleException("Perfil no encontrado"));

        if (request.nombre_perfil() == null || request.nombre_perfil().trim().isEmpty()) {
            throw new BusinessRuleException("El nombre de usuario no puede estar vacío.");
        }

        // Actualizar campos
        perfil.setBiografia(request.biografia());
        perfil.setUbicacion(request.ubicacion());
        perfil.setFotoPerfil(request.foto_perfil());

        perfilRepository.save(perfil);

        // Convertir a Response usando constructor inmutable
        return mapToResponse(perfil);
    }

    @Transactional
    public AuthResponseDTO actualizarCred(CredentialsRequestDTO req) {
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

    private PerfilResponseDTO mapToResponse(Perfil perfil) {
        return new PerfilResponseDTO(
                perfil.getIdPerfil(),
                perfil.getPersona().getNombres(),
                perfil.getPersona().getGenero()
        );
    }
}
