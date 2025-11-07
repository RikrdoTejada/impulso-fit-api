package com.impulsofit.service;

import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UsuarioResponseDTO create(UsuarioRequestDTO u) {
        Usuario usuario = new Usuario();
        // Mapear campos desde el DTO (en español) al modelo
        usuario.setNombres(u.nombres());
        usuario.setApellidoP(u.apellido_p());
        usuario.setApellidoM(u.apellido_m());
        usuario.setEmail(u.email());
        usuario.setContrasena(u.contrasena());
        usuario.setFechaNacimiento(u.fecha_nacimiento());
        usuario.setGenero(u.genero());
        usuario.setCodPregunta(u.cod_pregunta());

        // valores por defecto / auditoría
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        if (usuario.getBloqueado() == null) {
            usuario.setBloqueado(false);
        }
        if (usuario.getIntentosFallidos() == null) {
            usuario.setIntentosFallidos(0);
        }

        Usuario saved = repository.save(usuario);
        return toResponse(saved);
    }

    @Override
    public UsuarioResponseDTO update(Long id, UsuarioRequestDTO u) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Para PUT: reemplaza todos los campos (según DTO)
        usuario.setNombres(u.nombres());
        usuario.setApellidoP(u.apellido_p());
        usuario.setApellidoM(u.apellido_m());
        usuario.setEmail(u.email());
        usuario.setContrasena(u.contrasena());
        usuario.setFechaNacimiento(u.fecha_nacimiento());
        usuario.setGenero(u.genero());
        usuario.setCodPregunta(u.cod_pregunta());
        // fechaRegistro se mantiene (no se pisa)
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }

        Usuario saved = repository.save(usuario);
        return toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public UsuarioResponseDTO getById(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return toResponse(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> list() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // --------- Helpers ---------

    private UsuarioResponseDTO toResponse(Usuario u) {
        return new UsuarioResponseDTO(
                getIdSafe(u),
                getUsernameSafe(u),
                u.getEmail(),
                toInstantSafe(u.getFechaRegistro()),   // createdAt (Instant) derivado de fechaRegistro
                u.getFechaNacimiento(),                // fecha_Nacimiento
                u.getGenero(),
                u.getFechaRegistro(),                  // fecha_creacion (LocalDateTime)
                u.getCodPregunta()
        );
    }

    private Long getIdSafe(Usuario u) {
        try {
            return (Long) Usuario.class.getMethod("getId").invoke(u);
        } catch (Exception e) {
            try {
                return (Long) Usuario.class.getMethod("getIdUsuario").invoke(u);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private String getUsernameSafe(Usuario u) {
        try {
            // si existe getNombreCompleto() úsalo como "username"
            Object v = Usuario.class.getMethod("getNombreCompleto").invoke(u);
            return v == null ? null : v.toString();
        } catch (Exception ignore) {
            // fallback: usa solo nombres
            try {
                Object v = Usuario.class.getMethod("getNombres").invoke(u);
                return v == null ? null : v.toString();
            } catch (Exception e) {
                return null;
            }
        }
    }

    private Instant toInstantSafe(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atZone(ZoneId.systemDefault()).toInstant();
    }
}