package com.impulsofit.controller;

import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.service.RetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/retos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminRetoController {

    private final RetoService retoService;

    @GetMapping
    public ResponseEntity<List<RetoResponseDTO>> findAll() {
        return ResponseEntity.ok(retoService.findAll());
    }

    @GetMapping("/busqueda/por-grupo/{id}")
    public ResponseEntity<List<RetoResponseDTO>> findByGrupoId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByGrupo_Id_grupo(id));
    }

    @GetMapping("/busqueda/por-creador/{id}")
    public ResponseEntity<List<RetoResponseDTO>> findByCreadorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByCreador_Id(id));
    }

    @GetMapping("/busqueda/por-unidad/{id}")
    public ResponseEntity<List<RetoResponseDTO>> findByUnitId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByUnidad_IdUnidad(id));
    }

}
