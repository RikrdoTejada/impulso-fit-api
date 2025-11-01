package com.impulsofit.controller;


import com.impulsofit.dto.request.UnidadRequestDTO;
import com.impulsofit.dto.response.UnidadResponseDTO;
import com.impulsofit.service.UnidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unit")
@RequiredArgsConstructor
public class UnidadController {
    private final UnidadService unidadService;

    @PostMapping
    public ResponseEntity<UnidadResponseDTO> create(@RequestBody UnidadRequestDTO u) {
        UnidadResponseDTO saved = unidadService.create(u);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
