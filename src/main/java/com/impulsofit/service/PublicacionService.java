package com.impulsofit.service;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class PublicacionService {
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;

    private final PublicacionRepository posts;
    private final UsuarioRepository users;
    private final GrupoRepository groups;
    private final RetoRepository challenges;

    public PublicacionService(
            PublicacionRepository posts,
            UsuarioRepository users,
            GrupoRepository groups,
            RetoRepository challenges
    ) {
        this.posts = posts;
        this.users = users;
        this.groups = groups;
        this.challenges = challenges;
    }

    @Transactional
    public PublicacionResponseDTO create(CrearPublicacionRequestDTO req) {
        // req.userId(), req.content(), req.challengeId(), req.groupId()
        Usuario usuario = users.findById(req.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.userId()));

        Publicacion publicacion = new Publicacion();
        publicacion.setUsuario(usuario);
        publicacion.setContenido(req.content());
        publicacion.setCreatedAt(Instant.now());
    public PublicacionResponseDTO create(PublicacionRequestDTO publicacion) {
        //Usuario
        Usuario usuario = usuarioRepository.findById(publicacion.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        //Contenido no puede estar vacio
        if(publicacion.contenido()==null){
            throw new BusinessRuleException("El contenido no puede estar vacio.");
        if (req.challengeId() != null) {
            Reto reto = challenges.findById(req.challengeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Challenge not found with id: " + req.challengeId()));
            publicacion.setReto(reto);
        }
        //Contenido no supera los 500 char
        if(publicacion.contenido().length()>500){
            throw new BusinessRuleException("El contenido no puede ser mayor a 500 caracteres." +
                    " Longitud actual: " +publicacion.contenido().length());

        if (req.groupId() != null) {
            Grupo grupo = groups.findById(req.groupId())
                    .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + req.groupId()));
            publicacion.setGrupo(grupo);
        }

        Publicacion publicacionEntity = new Publicacion();
        publicacionEntity.setUsuario(usuario);

        //Grupo y Tipo
        if (publicacion.id_grupo() == null) {
            publicacionEntity.setGrupo(null);
            publicacionEntity.setType(PublicacionType.GENERAL);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            //Membresia de Grupo
            boolean member = membresiaGrupoRepository.
                    existsByUsuario_IdUsuarioAndGrupo_IdGrupo(publicacion.id_usuario(), publicacion.id_grupo());
            if (!member) throw new BusinessRuleException("El usuario no pertenece al grupo");
            publicacionEntity.setGrupo(grupo);
            publicacionEntity.setType(PublicacionType.GROUP);
        if (publicacion.getReto() == null && publicacion.getGrupo() == null) {
            throw new IllegalArgumentException("Post must belong to a group or challenge");
        }

        //Contenido
        publicacionEntity.setContenido(publicacion.contenido());

        Publicacion saved = publicacionRepository.save(publicacionEntity);

        return mapToResponse(saved);
    }
        Publicacion saved = posts.save(publicacion);

        return new PublicacionResponseDTO(
                saved.getId(),
                saved.getUsuario().getId(),
                saved.getContenido(),
                saved.getReto() != null ? saved.getReto().getId() : null,
                saved.getGrupo().getId(),
                saved.getCreatedAt()
        );
    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> findAll() {
        return publicacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> listAll() {
        return posts.findAll().stream()
                .map(p -> new PublicacionResponseDTO(
                        p.getId(),
                        p.getUsuario().getId(),
                        p.getContenido(),
                        p.getReto() != null ? p.getReto().getId() : null,
                        p.getGrupo().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    public List<PublicacionResponseDTO> findByUsuario_IdUsuario(Long id_usuario) {
        return publicacionRepository.findAllByUsuario_IdUsuario(id_usuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> findByGrupo_IdGrupo(Long id_grupo) {
        return publicacionRepository.findAllByGrupo_IdGrupo(id_grupo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PublicacionResponseDTO update(Long id, PublicacionRequestDTO publicacion) {
        Publicacion publicacionEntity = publicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la publicacion con id " + id));
        publicacionEntity.setIdPublicacion(id);
    public List<PublicacionResponseDTO> listByUser(Long userId) {
        users.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        //Usuario
        Usuario usuario = usuarioRepository.findById(publicacion.id_usuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        publicacionEntity.setUsuario(usuario);

        //Contenido no puede estar vacio
        if(publicacion.contenido()==null || publicacion.contenido().isBlank()){
            throw new BusinessRuleException("El contenido no puede estar vacio.");
        }

        //Contenido no supera los 500 char
        if(publicacion.contenido().length()>500){
            throw new BusinessRuleException("El contenido no puede ser mayor a 500 caracteres. " +
                    "Longitud actual: " +publicacion.contenido().length());
        }

        //Grupo y Tipo
        if (publicacion.id_grupo() == null) {
            publicacionEntity.setGrupo(null);
            publicacionEntity.setType(PublicacionType.GENERAL);
        } else {
            Grupo grupo = grupoRepository.findById(publicacion.id_grupo())
                    .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));
            //Membresia de Grupo
            boolean member = membresiaGrupoRepository.
                    existsByUsuario_IdUsuarioAndGrupo_IdGrupo(publicacion.id_usuario(), publicacion.id_grupo());
            if (!member) throw new BusinessRuleException("El usuario no pertenece al grupo");
            publicacionEntity.setGrupo(grupo);
            publicacionEntity.setType(PublicacionType.GROUP);
        }

        //Contenido
        publicacionEntity.setContenido(publicacion.contenido());

        Publicacion saved = publicacionRepository.save(publicacionEntity);

        return mapToResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!publicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la publicacion con el id: " + id);
        }
        publicacionRepository.deleteById(id);
    }

    private PublicacionResponseDTO mapToResponse(Publicacion publicacion) {
        boolean publica = (publicacion.getType() == PublicacionType.GENERAL);
        String grupoNombre = publica ? null : publicacion.getGrupo().getNombre();
        return new PublicacionResponseDTO(
                publicacion.getIdPublicacion(),
                publicacion.getUsuario().getNombres(),
                publicacion.getType(),
                grupoNombre,
                publicacion.getContenido(),
                publicacion.getFechaPublicacion()
        );
        return posts.findAllByUsuarioId(userId).stream()
                .map(p -> new PublicacionResponseDTO(
                        p.getId(),
                        p.getUsuario().getId(),
                        p.getContenido(),
                        p.getReto() != null ? p.getReto().getId() : null,
                        p.getGrupo().getId(),
                        p.getCreatedAt()
                ))
                .toList();
    }
}