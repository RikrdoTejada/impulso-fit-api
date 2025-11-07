package com.impulsofit.controller;

import com.impulsofit.dto.response.SeguidorResponseDTO;
import com.impulsofit.service.SeguidorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class SeguidorController {

    private final SeguidorService service;

    public SeguidorController(SeguidorService service) {
        this.service = service;
    }

    // Seguir a un usuario
    @PostMapping("/{userId}/follow/{targetId}")
    public ResponseEntity<SeguidorResponseDTO> follow(@PathVariable Long userId, @PathVariable Long targetId) {
        return ResponseEntity.ok(service.follow(userId, targetId));
    }

    // Dejar de seguir
    @DeleteMapping("/{userId}/follow/{targetId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long userId, @PathVariable Long targetId) {
        service.unfollow(userId, targetId);
        return ResponseEntity.noContent().build();
    }

    // Mis seguidores
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<SeguidorResponseDTO>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(service.followersOf(userId));
    }

    // A quién sigo
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<SeguidorResponseDTO>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(service.followingOf(userId));
    }
}