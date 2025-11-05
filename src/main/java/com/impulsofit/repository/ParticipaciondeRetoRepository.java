package com.impulsofit.repository;

import com.impulsofit.model.ParticipaciondeReto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipaciondeRetoRepository extends JpaRepository<ParticipaciondeReto, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
    Optional<ParticipaciondeReto> findByChallengeIdAndUserId(Long challengeId, Long userId);
    List<ParticipaciondeReto> findAllByChallengeId(Long challengeId);
}