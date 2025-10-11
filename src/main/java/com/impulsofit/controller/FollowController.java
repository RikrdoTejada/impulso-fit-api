package com.impulsofit.controller;

import com.impulsofit.model.Follow;
import com.impulsofit.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService service;

    // Seguir usuario
    @PostMapping("/follow")
    public ResponseEntity<?> seguir(@PathVariable Long userId, @RequestParam Long seguidoId) {
        Follow f = service.seguir(userId, seguidoId);
        if (f == null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya lo sigues");
        return ResponseEntity.status(HttpStatus.CREATED).body(f);
    }

    // Dejar de seguir
    @PostMapping("/unfollow")
    public ResponseEntity<?> dejarDeSeguir(@PathVariable Long userId, @RequestParam Long seguidoId) {
        service.dejarDeSeguir(userId, seguidoId);
        return ResponseEntity.ok("Has dejado de seguir al usuario");
    }
}