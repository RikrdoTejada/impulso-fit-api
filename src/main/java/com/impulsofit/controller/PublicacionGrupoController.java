package com.impulsofit.controller;

import com.impulsofit.model.PublicacionGrupo;
import com.impulsofit.repository.PublicacionGrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/publicaciones-grupo")
public class PublicacionGrupoController {

    @Autowired
    private PublicacionGrupoRepository publicacionGrupoRepository;

    // Obtener publicaciones de un grupo específico
    @GetMapping("/{grupoId}")
    public List<PublicacionGrupo> obtenerPublicacionesPorGrupo(@PathVariable Long grupoId) {
        return publicacionGrupoRepository.findByGrupoIdOrderByFechaCreacionDesc(grupoId);
    }
}
