package com.impulsofit.controller;

import com.impulsofit.dto.response.BusquedaResponseDTO;
import com.impulsofit.service.BusquedaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buscar")
public class BusquedaController {

    private final BusquedaService busquedaService;

    public BusquedaController(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    @GetMapping
    public ResponseEntity<BusquedaResponseDTO> buscar(@RequestParam String termino, @RequestParam(required = false) Integer deporteId) {
        BusquedaResponseDTO resultados = busquedaService.buscar(termino, deporteId);
        boolean empty = (resultados.getGrupos() == null || resultados.getGrupos().isEmpty())
                && (resultados.getUsuarios() == null || resultados.getUsuarios().isEmpty())
                && (resultados.getRetos() == null || resultados.getRetos().isEmpty());
        if (empty) {
            return ResponseEntity.ok().header("X-Search-Message", "No se encontraron resultados").body(resultados);
        }
        return ResponseEntity.ok(resultados);
    }
}
