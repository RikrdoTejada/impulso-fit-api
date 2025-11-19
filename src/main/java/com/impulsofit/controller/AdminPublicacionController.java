package com.impulsofit.controller;

import com.impulsofit.dto.response.PublicacionResponseDTO;
import com.impulsofit.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/publicacion")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPublicacionController {

    private final PublicacionService publicacionService;

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
}
