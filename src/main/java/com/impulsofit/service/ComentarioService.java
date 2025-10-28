package com.impulsofit.service;

import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.model.PublicacionGeneral;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ComentarioRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionGeneralRepository;
import com.impulsofit.repository.PublicacionGrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final PublicacionGeneralRepository publicacionGeneralRepo;
    private final PublicacionGrupoRepository publicacionGrupoRepo;
    private final UsuarioRepository usuarioRepo;
    private final MembresiaGrupoRepository membresiaRepo;

    private static final Pattern PROHIBITED_EXT_PATTERN = Pattern.compile("(?i)\\.(png|jpg|jpeg|gif|bmp|svg|webp|pdf)\\b");
    private static final Pattern URL_PATTERN = Pattern.compile("(?i)https?://|www\\.");

    public ComentarioService(ComentarioRepository comentarioRepository,
                              PublicacionGeneralRepository publicacionGeneralRepo,
                              PublicacionGrupoRepository publicacionGrupoRepo,
                              UsuarioRepository usuarioRepo,
                              MembresiaGrupoRepository membresiaRepo) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionGeneralRepo = publicacionGeneralRepo;
        this.publicacionGrupoRepo = publicacionGrupoRepo;
        this.usuarioRepo = usuarioRepo;
        this.membresiaRepo = membresiaRepo;
    }

    public List<Comentario> listarPorPublicacion(Long publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId);
    }

    // Listar por tipo
    public List<Comentario> listarPorPublicacionGeneral(Long publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId);
    }

    public List<Comentario> listarPorPublicacionGrupo(Long publicacionId) {
        return comentarioRepository.findByPublicacionId(publicacionId);
    }

    // Crear comentario en publicación general
    public Comentario crearComentarioEnPublicacionGeneral(Long usuarioId, Long publicacionId, String contenido) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuario inválido");
        if (publicacionId == null) throw new IllegalArgumentException("Publicación inválida");

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<PublicacionGeneral> publicacionOpt = publicacionGeneralRepo.findById(publicacionId);
        if (publicacionOpt.isEmpty()) throw new IllegalArgumentException("Publicación no encontrada");

        validarComentario(contenido);

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setUsuario(usuarioOpt.get());
        comentario.setPublicacion(publicacionOpt.get());

        return comentarioRepository.save(comentario);
    }

    // Crear comentario en publicación grupal (valida membresía)
    public Comentario crearComentarioEnPublicacionGrupo(Long usuarioId, Long publicacionId, String contenido) {
        if (usuarioId == null) throw new IllegalArgumentException("Usuario inválido");
        if (publicacionId == null) throw new IllegalArgumentException("Publicación inválida");

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) throw new IllegalArgumentException("Usuario no encontrado");

        Optional<PublicacionGrupo> publicacionOpt = publicacionGrupoRepo.findById(publicacionId);
        if (publicacionOpt.isEmpty()) throw new IllegalArgumentException("Publicación no encontrada");

        PublicacionGrupo pg = publicacionOpt.get();
        if (pg.getGrupo() == null || pg.getGrupo().getId() == null) {
            throw new IllegalArgumentException("Publicación de grupo con datos de grupo inválidos (RN-17)");
        }

        boolean esMiembro = membresiaRepo.existsByUsuario_IdAndGrupo_Id(usuarioId, pg.getGrupo().getId());
        if (!esMiembro) throw new IllegalArgumentException("El usuario debe ser miembro del grupo para comentar");

        validarComentario(contenido);

        Comentario comentario = new Comentario();
        comentario.setContenido(contenido);
        comentario.setUsuario(usuarioOpt.get());
        comentario.setPublicacion(pg);

        return comentarioRepository.save(comentario);
    }

    public Comentario crearComentario(Comentario comentario) {
        // Validaciones básicas de entrada
        if (comentario.getUsuario() == null || comentario.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuario inválido");
        }
        if (comentario.getPublicacion() == null || comentario.getPublicacion().getId() == null) {
            throw new IllegalArgumentException("Publicación inválida");
        }

        Long usuarioId = comentario.getUsuario().getId();
        Long publicacionId = comentario.getPublicacion().getId();

        Optional<Usuario> usuarioOpt = usuarioRepo.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        // Intentar cargar la publicación primero desde publicaciongeneral
        Optional<PublicacionGeneral> publicacionGeneralOpt = publicacionGeneralRepo.findById(publicacionId);
        PublicacionGeneral publicacion = null;
        if (publicacionGeneralOpt.isPresent()) {
            publicacion = publicacionGeneralOpt.get();
        } else {
            // Intentar cargar desde publicación de grupo
            Optional<PublicacionGrupo> publicacionGrupoOpt = publicacionGrupoRepo.findById(publicacionId);
            if (publicacionGrupoOpt.isPresent()) {
                publicacion = publicacionGrupoOpt.get();
            }
        }

        if (publicacion == null) {
            throw new IllegalArgumentException("Publicación no encontrada");
        }

        Usuario usuario = usuarioOpt.get();

        // Si es una publicación de grupo, validar membresía
        if (publicacion instanceof PublicacionGrupo) {
            PublicacionGrupo pg = (PublicacionGrupo) publicacion;
            if (pg.getGrupo() == null || pg.getGrupo().getId() == null) {
                throw new IllegalArgumentException("Publicación de grupo con datos de grupo inválidos (RN-17)");
            }
            boolean esMiembro = membresiaRepo.existsByUsuario_IdAndGrupo_Id(usuarioId, pg.getGrupo().getId());
            if (!esMiembro) {
                throw new IllegalArgumentException("El usuario debe ser miembro del grupo para comentar (RN-01)");
            }
        }

        validarComentario(comentario.getContenido());

        comentario.setUsuario(usuario);
        comentario.setPublicacion(publicacion);

        return comentarioRepository.save(comentario);
    }

    private String contenidoFrom(Comentario comentario) {
        return comentario.getContenido();
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

    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
