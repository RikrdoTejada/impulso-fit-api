package com.impulsofit.controller;

import com.impulsofit.dto.request.MembresiaGrupoRequest;
import com.impulsofit.dto.response.MembresiaGrupoResponse;
import com.impulsofit.service.MembresiaGrupoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembresiaGrupoController {
    private final MembresiaGrupoService membresiaGrupoService;

    @PostMapping
    public ResponseEntity<MembresiaGrupoResponse> create(@RequestBody MembresiaGrupoRequest m) {
        MembresiaGrupoResponse saved = membresiaGrupoService.create(m);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        membresiaGrupoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
