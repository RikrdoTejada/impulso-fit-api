package com.impulsofit.controller;

import com.impulsofit.dto.request.PublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequestMapping("/publicacion")
@RequiredArgsConstructor
public class PublicacionController {
    private final PublicacionService publicacionService;

    private final PublicacionService service;

    public PublicacionController(PublicacionService service) {
        this.service = service;
    }

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<PublicacionResponseDTO> create(@Valid @RequestBody CrearPublicacionRequestDTO req) {
        PublicacionResponseDTO response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    public ResponseEntity<PublicacionResponseDTO> create(@RequestBody PublicacionRequestDTO p) {
        PublicacionResponseDTO saved = publicacionService.create(p);
        return ResponseEntity.ok(saved);
    }

    @GetMapping({"busqueda/"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAll() {
        return ResponseEntity.ok(publicacionService.findAll());
    }

    @GetMapping({"busqueda/por-group/{id}"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAllByGrupo_IdGrupo(@PathVariable Long id)
    {
        return ResponseEntity.ok(publicacionService.findByGrupo_IdGrupo(id));
    }

    @GetMapping({"busqueda/por-usuario/{id}"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAllByUser_Id(@PathVariable Long id)
    {
        return ResponseEntity.ok(publicacionService.findByUsuario_IdUsuario(id));
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