package com.impulsofit.service;

import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Deporte;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.DeporteRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DeporteRepository deporteRepository;

    // Listar todos los grupos
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }
    private final GrupoRepository groups;
    private final UsuarioRepository users;

    // Buscar grupos por nombre o deporte
    public List<Grupo> buscarPorNombreODeporte(String filtro) {
        return grupoRepository.buscarPorNombreODeporte(filtro);
    public GrupoService(GrupoRepository groups, UsuarioRepository users) {
        this.groups = groups;
        this.users = users;
    }

    @Transactional
    public GrupoResponseDTO create(CrearGrupoRequestDTO req) {
        Usuario creator = users.findById(req.getCreatedById())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + req.getCreatedById()));
    // Crear un nuevo grupo
    public GrupoResponseDTO create(GrupoRequestDTO grupo) {

        Usuario usuario = usuarioRepository.findById(grupo.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado"));

        Deporte deporte = deporteRepository.findById(grupo.id_deporte())
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado"));
        Grupo saved = groups.save(g);

        Grupo grupoEntity = new Grupo();
        grupoEntity.setCreador(usuario); // Alias para usuarioCreador
        grupoEntity.setDeporte(deporte);
        grupoEntity.setNombre(grupo.nombre());
        grupoEntity.setDescripcion(grupo.descripcion());
        grupoEntity.setUbicacion(grupo.ubicacion());

        Grupo saved = grupoRepository.save(grupoEntity);
        return new GrupoResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getCreatedBy().getId(),
                saved.getCreatedBy().getUsername(),
                saved.getCreatedAt()
                saved.getIdGrupo(),
                saved.getNombre(),
                saved.getDeporte() != null ? saved.getDeporte().getNombre() : null,
                saved.getDescripcion(),
                "/grupos/" + saved.getIdGrupo() + "/unirse",
                saved.getUbicacion(),
                (saved.getFechaCreacion() == null) ? null : saved.getFechaCreacion().toLocalDate()
        );
    }

    // Borrar grupo por id
    public void delete(Long id) {
        if (!grupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el grupo con el id: " + id);
        }
        grupoRepository.deleteById(id);
    }
}

