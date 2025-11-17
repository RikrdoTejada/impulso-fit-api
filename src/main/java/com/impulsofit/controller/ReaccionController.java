package com.impulsofit.controller;

import com.impulsofit.dto.request.ReaccionRequestDTO;
import com.impulsofit.dto.response.ReaccionResponseDTO;
import com.impulsofit.service.ReaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reaccion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ReaccionController {
    private final ReaccionService reaccionService;

    @PostMapping
    public ResponseEntity<ReaccionResponseDTO> create(@RequestBody ReaccionRequestDTO r) {
        ReaccionResponseDTO saved = reaccionService.create(r);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ReaccionResponseDTO> delete(@PathVariable Long id) {
        reaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
