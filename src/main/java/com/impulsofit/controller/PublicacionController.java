package com.impulsofit.controller;

import com.impulsofit.dto.request.PublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publicacion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class PublicacionController {
    private final PublicacionService publicacionService;

    @PostMapping
    public ResponseEntity<PublicacionResponseDTO> create(@RequestBody PublicacionRequestDTO p) {
        PublicacionResponseDTO saved = publicacionService.create(p);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("{id}")
    public ResponseEntity<PublicacionResponseDTO> update(@PathVariable Long id, @RequestBody PublicacionRequestDTO p) {
        return ResponseEntity.ok(publicacionService.update(id, p));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publicacionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}