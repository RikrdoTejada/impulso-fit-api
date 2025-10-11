package com.impulsofit.repository;

import com.impulsofit.model.Post;
import com.impulsofit.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGrupo(Group grupo);
}