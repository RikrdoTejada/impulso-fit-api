package com.impulsofit.service;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Seguido;
import com.impulsofit.repository.SeguidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FeedGeneralService {

    private final SeguidoRepository seguidoRepository;
    private final EntityManager em;

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> obtenerFeedGeneralPorPerfil(Long idPerfilSeguidor) {
        List<Seguido> segs = seguidoRepository.findAllById_IdSeguidorOrderByFechaSeguidoDesc(idPerfilSeguidor);
        List<Long> idsSeguidos = segs.stream().map(s -> s.getId().getIdSeguido()).collect(Collectors.toList());
        if (idsSeguidos.isEmpty()) return List.of();

        TypedQuery<Publicacion> q = em.createQuery(
                "SELECT p FROM Publicacion p WHERE p.perfil.idPerfil IN :ids ORDER BY p.fechaPublicacion DESC",
                Publicacion.class
        );
        q.setParameter("ids", idsSeguidos);
        List<Publicacion> rows = q.getResultList();

        List<PublicacionResponseDTO> result = new ArrayList<>();
        for (Publicacion p : rows) {
            Long id = p.getIdPublicacion();
            String autor = null;
            if (p.getPerfil() != null) {
                if (p.getPerfil().getNombrePerfil() != null && !p.getPerfil().getNombrePerfil().isBlank()) {
                    autor = p.getPerfil().getNombrePerfil();
                } else if (p.getPerfil().getPersona() != null) {
                    autor = p.getPerfil().getPersona().getNombres();
                }
            }
            String grupoNombre = p.getGrupo() != null ? p.getGrupo().getNombre() : null;
            String contenido = p.getContenido();
            LocalDateTime fecha = p.getFechaPublicacion();
            result.add(new PublicacionResponseDTO(id, autor, p.getType(), grupoNombre, contenido, fecha));
        }

        return result;
    }
}

