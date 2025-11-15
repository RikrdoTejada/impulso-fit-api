package com.impulsofit.service;

import com.impulsofit.dto.request.GrupoRequestDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Deporte;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.DeporteRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final DeporteRepository deporteRepository;
    private final PerfilRepository perfilRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;

    // Listar todos los grupos
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    // Buscar grupos por nombre o deporte
    public List<Grupo> buscarPorNombreODeporte(String filtro) {
        return grupoRepository.buscarPorNombreODeporte(filtro);
    }

    // Crear un nuevo grupo
    public GrupoResponseDTO create(GrupoRequestDTO grupo) {

        Perfil perfil = perfilRepository.findById(grupo.id_perfil_creador())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil creador no encontrado"));

        Deporte deporte = deporteRepository.findById(grupo.id_deporte())
                .orElseThrow(() -> new ResourceNotFoundException("Deporte no encontrado"));

        Grupo grupoEntity = new Grupo();
        grupoEntity.setPerfilCreador(perfil); // Alias para usuarioCreador
        grupoEntity.setDeporte(deporte);
        grupoEntity.setNombre(grupo.nombre());
        grupoEntity.setDescripcion(grupo.descripcion());
        grupoEntity.setUbicacion(grupo.ubicacion());

        Grupo saved = grupoRepository.save(grupoEntity);

        // Crear membresía del creador
        MembresiaGrupo memb = new MembresiaGrupo();
        memb.setPerfil(perfil);
        memb.setGrupo(saved);
        membresiaGrupoRepository.save(memb);

        return new GrupoResponseDTO(
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