// java
package com.impulsofit.service;

import com.impulsofit.model.Comentario;
import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.repository.PublicacionGrupoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        List<PublicacionGrupo> publicaciones = publicacionGrupoRepository.findByGrupoId(grupoId);
        // cargar comentarios por cada publicacion
        return publicaciones;
    }

    @Transactional(readOnly = true)
    public List<Comentario> obtenerComentariosPorPublicacion(Long publicacionId) {
        TypedQuery<Comentario> q = em.createQuery(
                "SELECT c FROM Comentario c, PublicacionGrupo p WHERE c.idPublicacion = p.idPublicacion AND p.idPublicacion = :pubId ORDER BY c.fechaCreacion",
                Comentario.class);
        q.setParameter("pubId", publicacionId);
        return q.getResultList();
    }
}