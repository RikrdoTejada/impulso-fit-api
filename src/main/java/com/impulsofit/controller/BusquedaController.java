package com.impulsofit.controller;

import com.impulsofit.dto.response.BusquedaResponseDTO;
import com.impulsofit.service.BusquedaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/buscar")
@PreAuthorize("hasRole('USER')")
public class BusquedaController {

    private final BusquedaService busquedaService;

    @GetMapping
    public ResponseEntity<BusquedaResponseDTO> buscar(@RequestParam String termino, @RequestParam(required = false) Integer deporteId) {
        BusquedaResponseDTO resultados = busquedaService.buscar(termino, deporteId);
        boolean empty = (resultados.grupos() == null || resultados.grupos().isEmpty())
                && (resultados.perfiles() == null || resultados.perfiles().isEmpty())
                && (resultados.retos() == null || resultados.retos().isEmpty());
        if (empty) {
            return ResponseEntity.ok().header("X-Search-Message", "No se encontraron resultados").body(resultados);
        }
        return ResponseEntity.ok(resultados);
    }
}
