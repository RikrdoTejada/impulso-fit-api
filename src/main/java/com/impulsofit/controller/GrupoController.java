package com.impulsofit.controller;
import com.impulsofit.dto.request.GrupoRequestDTO;
import com.impulsofit.dto.request.MembresiaGrupoRequestDTO;
import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.dto.response.MembresiaGrupoResponseDTO;
import com.impulsofit.service.GrupoService;
import com.impulsofit.service.MembresiaGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grupo")
@RequiredArgsConstructor
public class GrupoController {
    private final GrupoService grupoService;
    private final MembresiaGrupoService membresiaGrupoService;

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
    @PostMapping("/unirse")
    public ResponseEntity<MembresiaGrupoResponseDTO> unirse_reto(@RequestBody MembresiaGrupoRequestDTO m) {
        MembresiaGrupoResponseDTO saved = membresiaGrupoService.create(m);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/dejar/{id}")
    public ResponseEntity<Void> dejar_grupo(@PathVariable Long id) {
        membresiaGrupoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}