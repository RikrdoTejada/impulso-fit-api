package com.impulsofit.controller;

import com.impulsofit.dto.response.FollowResponse;
import com.impulsofit.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class FollowController {

    private final FollowService service;

    public FollowController(FollowService service) {
        this.service = service;
    }

    // Seguir a un usuario
    @PostMapping("/{userId}/follow/{targetId}")
    public ResponseEntity<FollowResponse> follow(@PathVariable Long userId, @PathVariable Long targetId) {
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
    public ResponseEntity<List<FollowResponse>> followers(@PathVariable Long userId) {
        return ResponseEntity.ok(service.followersOf(userId));
    }

    // A quién sigo
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponse>> following(@PathVariable Long userId) {
        return ResponseEntity.ok(service.followingOf(userId));
    }
}