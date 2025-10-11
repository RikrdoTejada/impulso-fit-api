package com.impulsofit.controller;


import com.impulsofit.dto.request.UnidadRequest;
import com.impulsofit.dto.response.UnidadResponse;
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
    public ResponseEntity<UnidadResponse> create(@RequestBody UnidadRequest u) {
        UnidadResponse saved = unidadService.create(u);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
