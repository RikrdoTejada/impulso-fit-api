package com.impulsofit.repository;

import com.impulsofit.model.Follow;
import com.impulsofit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsBySeguidorAndSeguido(User seguidor, User seguido);
    void deleteBySeguidorAndSeguido(User seguidor, User seguido);
}