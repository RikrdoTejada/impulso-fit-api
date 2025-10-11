package com.impulsofit.repository;

import com.impulsofit.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    Optional<Follow> findByFollower_IdAndFollowing_Id(Long followerId, Long followingId);

    List<Follow> findByFollower_Id(Long followerId);   // a quién sigo
    List<Follow> findByFollowing_Id(Long followingId); // mis seguidores
}