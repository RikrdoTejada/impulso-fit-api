package com.impulsofit.controller;

import com.impulsofit.dto.request.CrearPublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.PublicacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PublicacionController {

    private final PublicacionService service;

    public PublicacionController(PublicacionService service) {
        this.service = service;
    }

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<PublicacionResponseDTO> create(@Valid @RequestBody CrearPublicacionRequestDTO req) {
        PublicacionResponseDTO response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar todos los posts
    @GetMapping
    public ResponseEntity<List<PublicacionResponseDTO>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    // Listar posts por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PublicacionResponseDTO>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.listByUser(userId));
    }
}