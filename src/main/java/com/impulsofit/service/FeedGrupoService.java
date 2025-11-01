// java
package com.impulsofit.service;

import com.impulsofit.dto.response.PublicacionGrupoResponseDTO;
import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.repository.PublicacionGrupoRepository;
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

    private final PublicacionGrupoRepository publicacionGrupoRepository;
    private final EntityManager em;

    public FeedGrupoService(PublicacionGrupoRepository publicacionGrupoRepository, EntityManager em) {
        this.publicacionGrupoRepository = publicacionGrupoRepository;
        this.em = em;
    }

    @Transactional(readOnly = true)
    public List<PublicacionGrupo> obtenerFeedPorGrupo(Long grupoId) {
        // devolver directamente las publicaciones del repositorio
        return publicacionGrupoRepository.findByGrupoId(grupoId);
    }

    @Transactional(readOnly = true)
    public List<Comentario> obtenerComentariosPorPublicacion(Long publicacionId) {
        TypedQuery<Comentario> q = em.createQuery(
                "SELECT c FROM Comentario c WHERE c.publicacion.id = :pubId ORDER BY c.fechaCreacion",
                Comentario.class);
        q.setParameter("pubId", publicacionId);
        return q.getResultList();
    }

    @Transactional(readOnly = true)
    public List<PublicacionGrupoResponseDTO> obtenerFeedDTOPorGrupo(Long grupoId) {
        String sql = "SELECT pg.id_publicacion, " +
                "COALESCE(p.contenido, pg.contenido) as contenido, " +
                "COALESCE(u.nombres, '') as autor, " +
                "COALESCE(p.fecha_publicacion, pg.fecha_publicacion) as fecha " +
                "FROM publicaciongrupo pg " +
                "LEFT JOIN publicaciongeneral p ON pg.id_publicacion = p.id_publicacion " +
                "LEFT JOIN usuario u ON (p.id_usuario = u.id_usuario OR pg.id_usuario = u.id_usuario) " +
                "WHERE pg.id_grupo = :grupoId " +
                "ORDER BY COALESCE(p.fecha_publicacion, pg.fecha_publicacion) DESC";

        var query = em.createNativeQuery(sql);
        query.setParameter("grupoId", grupoId);
        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<PublicacionGrupoResponseDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            Number idNum = (Number) row[0];
            Long id = idNum != null ? idNum.longValue() : null;
            String contenido = row[1] != null ? row[1].toString() : null;
            String autor = row[2] != null ? row[2].toString() : null;
            LocalDateTime fecha = null;
            if (row[3] instanceof Timestamp) {
                fecha = ((Timestamp) row[3]).toLocalDateTime();
            } else if (row[3] != null) {
                try {
                    fecha = LocalDateTime.parse(row[3].toString());
                } catch (Exception ignored) {}
            }

            // obtener comentarios por consulta nativa para evitar problemas con entidades huérfanas
            List<String> comentarios = Collections.emptyList();
            try {
                var q2 = em.createNativeQuery("SELECT contenido FROM comentario WHERE id_publicacion = :pubId AND tipo = 'GRUPAL' ORDER BY fecha_comentario");
                q2.setParameter("pubId", id);
                @SuppressWarnings("unchecked")
                List<Object> cmts = q2.getResultList();
                if (cmts != null && !cmts.isEmpty()) {
                    comentarios = cmts.stream().map(Object::toString).collect(Collectors.toList());
                }
            } catch (Exception ignored) {}

            result.add(new PublicacionGrupoResponseDTO(id, contenido, autor, fecha, comentarios));
        }

        return result;
    }
}