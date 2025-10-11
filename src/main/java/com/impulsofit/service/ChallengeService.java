package com.impulsofit.service;

import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepo;
    private final GroupRepository groupRepo;

    public List<Challenge> listarPorGrupo(Long groupId) {
        Group g = groupRepo.findById(groupId).orElseThrow();
        return challengeRepo.findByGrupo(g);
    }
}