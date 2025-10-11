package com.impulsofit.repository;

import com.impulsofit.model.Challenge;
import com.impulsofit.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByGrupo(Group grupo);
}