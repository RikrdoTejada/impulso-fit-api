package com.impulsofit.controller;

import com.impulsofit.dto.request.CreatePostRequest;
import com.impulsofit.dto.response.PostResponse;
import com.impulsofit.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    // Crear un nuevo post
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody CreatePostRequest req) {
        PostResponse response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Listar todos los posts
    @GetMapping
    public ResponseEntity<List<PostResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    // Listar posts por usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> listByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.listByUser(userId));
    }
}