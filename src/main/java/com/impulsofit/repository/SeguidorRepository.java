package com.impulsofit.repository;

import com.impulsofit.model.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {

    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    Optional<Seguidor> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    List<Seguidor> findByFollower_Id(Long followerId);   // a quién sigo
    List<Seguidor> findByFollowing_Id(Long followingId); // mis seguidores
}