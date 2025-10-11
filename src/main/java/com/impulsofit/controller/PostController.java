package com.impulsofit.controller;

import com.impulsofit.model.Post;
import com.impulsofit.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/groups/{groupId}/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService service;

    // Listar publicaciones de un grupo
    @GetMapping
    public List<Post> listar(@PathVariable Long groupId) {
        return service.listarPorGrupo(groupId);
    }

    // Publicar en foro de grupos
    @PostMapping
    public ResponseEntity<Post> publicar(@PathVariable Long groupId,
                                         @RequestParam Long userId,
                                         @RequestBody Map<String, String> body) {
        String contenido = body.getOrDefault("contenido", "");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.publicar(groupId, userId, contenido));
    }
}