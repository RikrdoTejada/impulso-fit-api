package com.impulsofit.controller;

import com.impulsofit.dto.request.RetoRequest;
import com.impulsofit.dto.response.RetoResponse;
import com.impulsofit.model.Reto;
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
    public ResponseEntity<List<Reto>> findAll() {
        return ResponseEntity.ok(retoService.findAll());
    }

    @PutMapping("{id}")
    public ResponseEntity<Reto> update(@PathVariable Long id, @RequestBody Reto r) {
        return ResponseEntity.ok(retoService.update(id, r));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        retoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
