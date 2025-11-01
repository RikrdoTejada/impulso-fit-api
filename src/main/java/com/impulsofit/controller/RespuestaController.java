package com.impulsofit.controller;

import com.impulsofit.dto.request.RespuestaRequestDTO;
import com.impulsofit.dto.response.RespuestaResponseDTO;
import com.impulsofit.service.RespuestaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answer")
@RequiredArgsConstructor
public class RespuestaController {
    private final RespuestaService respuestaService;

    @PostMapping
    public ResponseEntity<RespuestaResponseDTO> create(@RequestBody RespuestaRequestDTO r){
        RespuestaResponseDTO saved = respuestaService.create(r);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        respuestaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
