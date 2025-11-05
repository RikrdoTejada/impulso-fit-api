package com.impulsofit.controller;

import com.impulsofit.dto.request.GrupoRequestDTO;
import com.impulsofit.dto.response.GrupoPopularDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.dto.response.MembresiaGrupoResponseDTO;
import com.impulsofit.model.Grupo;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.service.GrupoMembresiaService;
import com.impulsofit.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/grupo")
public class GrupoController {

    private final GrupoService grupoService;
    private final GrupoRepository grupoRepository;
    private final GrupoMembresiaService grupoMembresiaService;

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> create(@RequestBody GrupoRequestDTO g) {
        GrupoResponseDTO saved = grupoService.create(g);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        grupoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Grupos populares para todos
    @GetMapping("/populares")
    public List<GrupoPopularDTO> obtenerGruposPopulares() {
        return grupoService.obtenerGruposPopulares();
    }

    // Grupos populares recomendados para un usuario específico
    @GetMapping("/populares/{idUsuario}")
    public List<GrupoPopularDTO> obtenerGruposPopularesParaUsuario(@PathVariable Long idUsuario) {
        return grupoService.obtenerGruposPopularesParaUsuario(idUsuario);
    }

    // Listar todos los grupos
    @GetMapping()
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    // Unirse a grupo
    @PostMapping("/{idGrupo}/unirse/{idUsuario}")
    public ResponseEntity<MembresiaGrupoResponseDTO> unirseAGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        MembresiaGrupoResponseDTO saved = grupoMembresiaService.unirseAGrupo(idGrupo, idUsuario);
        return ResponseEntity.ok(saved);
    }

    // Dejar grupo
    @PostMapping("/{idGrupo}/dejar/{idUsuario}")
    public ResponseEntity<Void> dejarGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        grupoMembresiaService.dejarGrupo(idUsuario, idGrupo);
        return ResponseEntity.noContent().build();
    }

    // Verificar membresía
    @GetMapping("/{idGrupo}/es-miembro/{idUsuario}")
    public boolean esMiembroDeGrupo(@PathVariable Long idGrupo, @PathVariable Long idUsuario) {
        return grupoMembresiaService.esMiembroDeGrupo(idUsuario, idGrupo);
    }

}