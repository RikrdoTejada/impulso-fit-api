package com.impulsofit.controller;

import com.impulsofit.model.User;
import com.impulsofit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public List<User> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<User> crear(@RequestBody User u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(u));
    }

    // Eliminar cuenta (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        service.darDeBaja(id);
        return ResponseEntity.ok("Cuenta eliminada correctamente");
    }
}