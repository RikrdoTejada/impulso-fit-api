package com.impulsofit.controller;

import com.impulsofit.dto.request.PublicacionRequestDTO;
import com.impulsofit.dto.response.PublicacionResponseDTO;
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
    public ResponseEntity<PublicacionResponseDTO> create(@RequestBody PublicacionRequestDTO p) {
        PublicacionResponseDTO saved = publicacionService.create(p);
        return ResponseEntity.ok(saved);
    }

    @GetMapping({"search/"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAll() {
        return ResponseEntity.ok(publicacionService.findAll());
    }

    @GetMapping({"search/by-group/{id}"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAllByGrupo_IdGrupo(@PathVariable Long id)
    {
        return ResponseEntity.ok(publicacionService.findByGrupo_IdGrupo(id));
    }

    @GetMapping({"search/by-user/{id}"})
    public ResponseEntity<List<PublicacionResponseDTO>> findAllByUser_Id(@PathVariable Long id)
    {
        return ResponseEntity.ok(publicacionService.findByUsuario_IdUsuario(id));
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