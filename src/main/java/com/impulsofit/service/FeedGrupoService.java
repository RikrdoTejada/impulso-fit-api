// java
package com.impulsofit.service;

import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.repository.PublicacionGrupoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FeedGrupoService {

    private final PublicacionGrupoRepository publicacionGrupoRepository;

    public FeedGrupoService(PublicacionGrupoRepository publicacionGrupoRepository) {
        this.publicacionGrupoRepository = publicacionGrupoRepository;
    }

    @Transactional(readOnly = true)
    public List<PublicacionGrupo> obtenerFeedPorGrupo(Long grupoId) {
        return publicacionGrupoRepository.findByGrupoId(grupoId);
    }
}