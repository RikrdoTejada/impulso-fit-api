package com.impulsofit.repository;

import com.impulsofit.model.ChallengeParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    boolean existsByChallengeIdAndUserId(Long challengeId, Long userId);
    Optional<ChallengeParticipation> findByChallengeIdAndUserId(Long challengeId, Long userId);
    List<ChallengeParticipation> findAllByChallengeId(Long challengeId);
}