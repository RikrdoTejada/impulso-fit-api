package com.impulsofit.controller;

import com.impulsofit.dto.request.PublicacionRequest;
import com.impulsofit.dto.response.PublicacionResponse;
import com.impulsofit.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PublicacionController {
    private final PublicacionService publicacionService;

    @PostMapping
    public ResponseEntity<PublicacionResponse> create(@RequestBody PublicacionRequest p) {
        PublicacionResponse saved = publicacionService.create(p);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<PublicacionResponse>> findAll() {
        return ResponseEntity.ok(publicacionService.findAll());
    }

    @PutMapping("{id}")
    public ResponseEntity<PublicacionResponse> update(@PathVariable Long id, @RequestBody PublicacionRequest p) {
        return ResponseEntity.ok(publicacionService.update(id, p));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}