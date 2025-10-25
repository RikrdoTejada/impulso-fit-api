package com.impulsofit.controller;

import com.impulsofit.dto.request.RetoRequest;
import com.impulsofit.dto.response.RetoResponse;
import com.impulsofit.service.RetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/challenge")
@RequiredArgsConstructor
public class RetoController {
    private final RetoService retoService;

    @PostMapping
    public ResponseEntity<RetoResponse> create(@RequestBody RetoRequest r) {
        RetoResponse saved = retoService.create(r);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<RetoResponse>> findAll() {
        return ResponseEntity.ok(retoService.findAll());
    }

    @GetMapping({"/search/by-group/{id}"})
    public ResponseEntity<List<RetoResponse>> findByGrupoId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByGrupo_Id_grupo(id));
    }

    @GetMapping({"/search/by-creator/{id}"})
    public ResponseEntity<List<RetoResponse>> findByCreadorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(retoService.findByCreador_Id(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<RetoResponse> update(@PathVariable Long id, @RequestBody RetoRequest r) {
        RetoResponse updated = retoService.update(id, r);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
