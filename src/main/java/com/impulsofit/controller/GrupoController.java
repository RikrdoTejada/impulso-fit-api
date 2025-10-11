package com.impulsofit.controller;

import com.impulsofit.model.Grupo;
import com.impulsofit.service.GrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoController {

    @Autowired
    private GrupoService grupoService;

    // Listar todos los grupos
    @GetMapping
    public ResponseEntity<List<Grupo>> listarGrupos() {
        return ResponseEntity.ok(grupoService.listarGrupos());
    }

    // Buscar grupos por nombre o deporte
    @GetMapping("/buscar")
    public ResponseEntity<List<Grupo>> buscarGrupos(@RequestParam String filtro) {
        List<Grupo> grupos = grupoService.buscarPorNombreODeporte(filtro);
        return ResponseEntity.ok(grupos);
    }
}
