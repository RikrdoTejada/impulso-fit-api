package com.impulsofit.controller;

import com.impulsofit.dto.request.MembresiaGrupoRequestDTO;
import com.impulsofit.dto.response.MembresiaGrupoResponseDTO;
import com.impulsofit.service.MembresiaGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membresia")
@RequiredArgsConstructor
public class MembresiaGrupoController {
    private final MembresiaGrupoService membresiaGrupoService;

    @PostMapping
    public ResponseEntity<MembresiaGrupoResponseDTO> create(@RequestBody MembresiaGrupoRequestDTO m) {
        MembresiaGrupoResponseDTO saved = membresiaGrupoService.create(m);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membresiaGrupoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}