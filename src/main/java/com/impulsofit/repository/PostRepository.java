package com.impulsofit.repository;

import com.impulsofit.model.Post;
import com.impulsofit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Por entidad usuario
    List<Post> findAllByUser(User user);

    // Por id de usuario (lo que usa el servicio)
    List<Post> findAllByUserId(Long userId);

    // Útiles para otros listados
    List<Post> findAllByGroupId(Long groupId);
    List<Post> findAllByChallengeId(Long challengeId);
}