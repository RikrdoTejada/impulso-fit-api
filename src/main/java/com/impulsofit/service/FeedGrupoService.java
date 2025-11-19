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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT " +
                "p.id_publicacion, " +                     // 0 - id publicación
                "per.nombres AS autor, " +                 // 1 - nombres persona
                "pe.nombre_perfil AS nombre_perfil, " +    // 2 - nombre perfil
                "g.nombre AS grupo_nombre, " +             // 3 - nombre grupo
                "p.contenido, " +                          // 4 - contenido
                "p.fecha_publicacion " +                   // 5 - fecha (timestamp)
                "FROM publicacion p " +
                "JOIN perfil pe ON p.id_perfil = pe.id_perfil " +
                "JOIN persona per ON pe.id_persona = per.id_persona " +
                "JOIN grupo g ON p.id_grupo = g.id_grupo " +
                "WHERE p.tipo_publicacion = :tipo " +
                "AND p.id_grupo = :grupoId " +
                "ORDER BY p.fecha_publicacion DESC";

        var query = em.createNativeQuery(sql);
        query.setParameter("tipo", PublicacionType.GROUP.name());
        query.setParameter("grupoId", grupoId);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm:ss");

        List<PublicacionResponseDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            // 0) ID
            Number idNum = (Number) row[0];
            Long id = (idNum != null) ? idNum.longValue() : null;

            // 1) Autor (nombres persona)
            String autor = (row[1] != null) ? row[1].toString() : null;

            // 2) Nombre de perfil
            String nombrePerfil = (row[2] != null) ? row[2].toString() : null;

            // 3) Nombre de grupo
            String grupoNombre = (row[3] != null) ? row[3].toString() : null;

            // 4) Contenido
            String contenido = (row[4] != null) ? row[4].toString() : null;

            // 5) Fecha → formateada como en mapToResponse
            String fechaFormateada = null;
            if (row[5] != null) {
                LocalDateTime fecha = null;
                if (row[5] instanceof Timestamp ts) {
                    fecha = ts.toLocalDateTime();
                } else {
                    try {
                        fecha = LocalDateTime.parse(row[5].toString());
                    } catch (Exception ignored) {}
                }
                if (fecha != null) {
                    fechaFormateada = dtf.format(fecha);
                }
            }

            // dto
            result.add(new PublicacionResponseDTO(
                    id,
                    autor,
                    nombrePerfil,
                    PublicacionType.GROUP,
                    grupoNombre,
                    contenido,
                    fechaFormateada
            ));
        }

        return result;
    }

}