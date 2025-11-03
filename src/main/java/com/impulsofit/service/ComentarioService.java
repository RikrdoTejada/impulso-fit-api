package com.impulsofit.service;

import com.impulsofit.dto.response.ComentarioResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ComentarioRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionRepository publicacionRepo;
    private final UsuarioRepository usuarioRepo;
    private final MembresiaGrupoRepository membresiaRepo;

    private static final Pattern PROHIBITED_EXT_PATTERN = Pattern.compile("(?i)\\.(png|jpg|jpeg|gif|bmp|svg|webp|pdf)\\b");
    private static final Pattern URL_PATTERN = Pattern.compile("(?i)https?://|www\\.");

    public ComentarioService(ComentarioRepository comentarioRepository,
                              PublicacionRepository publicacionRepo,
                              UsuarioRepository usuarioRepo,
                              MembresiaGrupoRepository membresiaRepo) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepo = publicacionRepo;
        this.usuarioRepo = usuarioRepo;
        this.membresiaRepo = membresiaRepo;
    }

    public List<Comentario> listarPorPublicacion(Long publicacionId) {
        return comentarioRepository.findByPublicacion_IdPublicacion(publicacionId);
    }

    public List<ComentarioResponseDTO> listarPorPublicacionDTO(Long publicacionId) {
        return listarPorPublicacion(publicacionId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ComentarioResponseDTO> listarPorPublicacionGeneralDTO(Long publicacionId) {
        return listarPorPublicacionGeneral(publicacionId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ComentarioResponseDTO> listarPorPublicacionGrupoDTO(Long publicacionId) {
        return listarPorPublicacionGrupo(publicacionId).stream().map(this::toDto).collect(Collectors.toList());
    }

    // Listar por tipo (entidades)
    public List<Comentario> listarPorPublicacionGeneral(Long publicacionId) {
        return comentarioRepository.findByPublicacion_IdPublicacion(publicacionId).stream()
                .filter(c -> "GENERAL".equalsIgnoreCase(c.getTipo()))
                .collect(Collectors.toList());
    }

    public List<Comentario> listarPorPublicacionGrupo(Long publicacionId) {
        return comentarioRepository.findByPublicacion_IdPublicacion(publicacionId).stream()
                .filter(c -> "GRUPAL".equalsIgnoreCase(c.getTipo()))
                .collect(Collectors.toList());
    }

    // Crear comentario en publicación general
    @Transactional
    public Comentario crearComentarioEnPublicacionGeneral(Long usuarioId, Long publicacionId, String contenido) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuario inválido");
        if (publicacionId == null) throw new IllegalArgumentException("Publicación inválida");

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Publicacion> publicacionOpt = publicacionRepo.findById(publicacionId);
        if (publicacionOpt.isEmpty()) throw new IllegalArgumentException("Publicación no encontrada");

        Publicacion publicacion = publicacionOpt.get();
        if (publicacion.getType() != PublicacionType.GENERAL) {
            throw new IllegalArgumentException("La publicación no es de tipo GENERAL");
        }

        validarComentario(contenido);

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setUsuario(usuarioOpt.get());
        comentario.setPublicacion(publicacion);
        comentario.setTipo("GENERAL");

        return comentarioRepository.save(comentario);
    }

    public ComentarioResponseDTO obtenerComentarioDTOPorId(Long id) {
        return comentarioRepository.findById(id).map(this::toDto).orElse(null);
    }

    // Crear comentario en publicación grupal (valida membresía)
    @Transactional
    public Comentario crearComentarioEnPublicacionGrupo(Long usuarioId, Long publicacionId, String contenido) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuario inválido");
        if (publicacionId == null) throw new IllegalArgumentException("Publicación inválida");

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<Publicacion> publicacionOpt = publicacionRepo.findById(publicacionId);
        if (publicacionOpt.isEmpty()) throw new IllegalArgumentException("Publicación no encontrada");

        Publicacion publicacion = publicacionOpt.get();
        if (publicacion.getType() != PublicacionType.GROUP) {
            throw new IllegalArgumentException("La publicación no es de tipo GRUPO");
        }

        if (publicacion.getGrupo() == null || publicacion.getGrupo().getIdGrupo() == null) {
            throw new IllegalArgumentException("Publicación de grupo con datos de grupo inválidos (RN-17)");
        }

        boolean esMiembro = membresiaRepo.existsByUsuario_IdAndGrupo_Id(usuarioId, publicacion.getGrupo().getIdGrupo());
        if (!esMiembro) throw new IllegalArgumentException("El usuario debe ser miembro del grupo para comentar");

        validarComentario(contenido);

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setUsuario(usuarioOpt.get());
        comentario.setPublicacion(publicacion);
        comentario.setTipo("GRUPAL");

        return comentarioRepository.save(comentario);
    }

    @Transactional
    public Comentario crearComentario(Comentario comentario) {
        // Validaciones básicas de entrada
        if (comentario.getUsuario() == null || comentario.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuario inválido");
        }
        if (comentario.getPublicacion() == null || comentario.getPublicacion().getIdPublicacion() == null) {
            throw new IllegalArgumentException("Publicación inválida");
        }

        Long usuarioId = comentario.getUsuario().getId();
        Long publicacionId = comentario.getPublicacion().getIdPublicacion();

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        Optional<Publicacion> publicacionOpt = publicacionRepo.findById(publicacionId);
        Publicacion publicacion = null;
        if (publicacionOpt.isPresent()) {
            publicacion = publicacionOpt.get();
        }

        if (publicacion == null) {
            throw new IllegalArgumentException("Publicación no encontrada");
        }

        Usuario usuario = usuarioOpt.get();

        // Si es una publicación de grupo, validar membresía
        if (publicacion.getType() == PublicacionType.GROUP) {
            if (publicacion.getGrupo() == null || publicacion.getGrupo().getIdGrupo() == null) {
                throw new IllegalArgumentException("Publicación de grupo con datos de grupo inválidos (RN-17)");
            }
            boolean esMiembro = membresiaRepo.existsByUsuario_IdAndGrupo_Id(usuarioId, publicacion.getGrupo().getIdGrupo());
            if (!esMiembro) {
                throw new IllegalArgumentException("El usuario debe ser miembro del grupo para comentar (RN-01)");
            }
        }

        validarComentario(comentario.getContenido());

        comentario.setUsuario(usuario);
        comentario.setPublicacion(publicacion);

        // Si no se especificó tipo, inferir del tipo de la publicación
        if (comentario.getTipo() == null) {
            comentario.setTipo(publicacion.getType() == PublicacionType.GROUP ? "GRUPAL" : "GENERAL");
        }

        return comentarioRepository.save(comentario);
    }

    private ComentarioResponseDTO toDto(Comentario c) {
        return new ComentarioResponseDTO(
                c.getId(),
                c.getContenido(),
                c.getUsuario() != null ? c.getUsuario().getNombres() : null,
                c.getFechaCreacion()
        );
    }

    private void validarComentario(String contenido) {
        if (!StringUtils.hasText(contenido)) {
            throw new IllegalArgumentException("El comentario no puede estar vacío");
        }

        if (contenido.length() > 300) {
            throw new IllegalArgumentException("El comentario no puede superar los 300 caracteres");
        }

        if (URL_PATTERN.matcher(contenido).find()) {
            throw new IllegalArgumentException("Los comentarios no pueden contener enlaces externos");
        }

        if (PROHIBITED_EXT_PATTERN.matcher(contenido).find()) {
            throw new IllegalArgumentException("Los comentarios no pueden contener referencias a archivos adjuntos o imágenes");
        }
    }

    @Transactional
    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
