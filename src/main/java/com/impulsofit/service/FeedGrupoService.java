// java
package com.impulsofit.service;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionType;
import com.impulsofit.repository.PublicacionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedGrupoService {

    private final PublicacionRepository publicacionRepository;
    private final EntityManager em;

    public FeedGrupoService(PublicacionRepository publicacionRepository, EntityManager em) {
        this.publicacionRepository = publicacionRepository;
        this.em = em;
    }

    @Transactional(readOnly = true)
    public List<Publicacion> obtenerFeedPorGrupo(Long grupoId) {
        // devolver directamente las publicaciones del repositorio filtrando por tipo GRUPO
        return publicacionRepository.findAllByTypeAndGrupo_IdGrupo(PublicacionType.GROUP, grupoId);
    }

    @Transactional(readOnly = true)
    public List<Comentario> obtenerComentariosPorPublicacion(Long publicacionId) {
        TypedQuery<Comentario> q = em.createQuery(
                "SELECT c FROM Comentario c WHERE c.publicacion.idPublicacion = :pubId ORDER BY c.fechaCreacion",
                Comentario.class);
        q.setParameter("pubId", publicacionId);
        return q.getResultList();
    }

    @Transactional(readOnly = true)
    public List<PublicacionResponseDTO> obtenerFeedDTOPorGrupo(Long grupoId) {
        String sql = "SELECT p.id_publicacion, p.contenido, " +
                "COALESCE(NULLIF(u.nombres, ''), '') as autor, " +
                "g.nombre as grupo_nombre, " +
                "p.fecha_publicacion " +
                "FROM publicacion p " +
                "LEFT JOIN usuario u ON p.id_usuario = u.id_usuario " +
                "LEFT JOIN grupo g ON p.id_grupo = g.id_grupo " +
                "WHERE p.tipo_publicacion = :tipo AND p.id_grupo = :grupoId " +
                "ORDER BY p.fecha_publicacion DESC";

        var query = em.createNativeQuery(sql);
        query.setParameter("tipo", PublicacionType.GROUP.name());
        query.setParameter("grupoId", grupoId);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<PublicacionResponseDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            Number idNum = (Number) row[0];
            Long id = idNum != null ? idNum.longValue() : null;
            String contenido = row[1] != null ? row[1].toString() : null;
            String autor = row[2] != null ? row[2].toString() : null;
            String grupoNombre = row[3] != null ? row[3].toString() : null;
            LocalDateTime fecha = null;
            if (row[4] instanceof Timestamp) {
                fecha = ((Timestamp) row[4]).toLocalDateTime();
            } else if (row[4] != null) {
                try {
                    fecha = LocalDateTime.parse(row[4].toString());
                } catch (Exception ignored) {}
            }

            result.add(new PublicacionResponseDTO(id, autor, PublicacionType.GROUP, grupoNombre, contenido, fecha));
        }

        return result;
    }
}