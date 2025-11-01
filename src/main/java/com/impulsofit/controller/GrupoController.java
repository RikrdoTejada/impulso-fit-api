package com.impulsofit.controller;
import com.impulsofit.dto.request.GrupoRequestDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.service.GrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GrupoController {
    private final GrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoResponseDTO> create(@RequestBody GrupoRequestDTO g) {
        GrupoResponseDTO saved = grupoService.create(g);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        grupoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}