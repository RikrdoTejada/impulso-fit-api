package com.impulsofit.service;

import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final GroupRepository groupRepo;
    private final UserRepository userRepo;

    public List<Post> listarPorGrupo(Long groupId) {
        Group g = groupRepo.findById(groupId).orElseThrow();
        return postRepo.findByGrupo(g);
    }

    public Post publicar(Long groupId, Long userId, String contenido) {
        Group g = groupRepo.findById(groupId).orElseThrow();
        User u = userRepo.findById(userId).orElseThrow();
        Post p = new Post();
        p.setContenido(contenido);
        p.setGrupo(g);
        p.setUser(u);
        return postRepo.save(p);
    }
}