package com.impulsofit.controller;

import com.impulsofit.model.Group;
import com.impulsofit.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;

    @GetMapping
    public List<Group> listar() { return service.listar(); }

    // Creación de grupos
    @PostMapping
    public ResponseEntity<Group> crear(@RequestBody Group g) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(g));
    }
}