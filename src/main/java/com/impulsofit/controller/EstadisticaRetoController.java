package com.impulsofit.controller;

import com.impulsofit.dto.response.DashboardRetoResponseDTO;
import com.impulsofit.service.EstadisticaRetoService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/estadisticas")
public class EstadisticaRetoController {

    private final EstadisticaRetoService estadisticaRetoService;

    public EstadisticaRetoController(EstadisticaRetoService estadisticaRetoService) {
        this.estadisticaRetoService = estadisticaRetoService;
    }

    @GetMapping("/reto/{idReto}/usuario/{idUsuario}")
    public DashboardRetoResponseDTO obtenerDashboardReto(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario) {
        return estadisticaRetoService.obtenerDashboardReto(idReto, idUsuario);
    }

    @PostMapping("/reto/{idReto}/progreso/{idUsuario}")
    public String registrarProgreso(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario,
            @RequestParam LocalDate fecha,
            @RequestParam boolean completado,
            @RequestParam(required = false) Integer puntos) {
        return estadisticaRetoService.registrarProgresoDiario(idReto, idUsuario, fecha, completado, puntos);
    }

    @GetMapping("/reto/{idReto}/mi-progreso/{idUsuario}")
    public Map<String, Object> obtenerMiProgreso(
            @PathVariable Long idReto,
            @PathVariable Long idUsuario) {
        return estadisticaRetoService.obtenerMiProgreso(idReto, idUsuario);
    }
}