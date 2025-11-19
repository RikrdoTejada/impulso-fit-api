package com.impulsofit.controller;

import com.impulsofit.service.EstadisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/perfiles")
@PreAuthorize("hasRole('USER')")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @GetMapping("/{idPerfil}/dias-progreso")
    public List<LocalDate> obtenerDiasProgreso(@PathVariable Long idPerfil) {
        return estadisticaService.diasConProgreso(idPerfil);
    }
}

