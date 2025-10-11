package com.impulsofit.service;

import com.impulsofit.model.Follow;
import com.impulsofit.model.User;
import com.impulsofit.repository.FollowRepository;
import com.impulsofit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    public Follow seguir(Long seguidorId, Long seguidoId) {
        User seguidor = userRepo.findById(seguidorId).orElseThrow();
        User seguido  = userRepo.findById(seguidoId).orElseThrow();
        if (followRepo.existsBySeguidorAndSeguido(seguidor, seguido)) return null;
        Follow f = new Follow();
        f.setSeguidor(seguidor);
        f.setSeguido(seguido);
        return followRepo.save(f);
    }

    public void dejarDeSeguir(Long seguidorId, Long seguidoId) {
        User seguidor = userRepo.findById(seguidorId).orElseThrow();
        User seguido  = userRepo.findById(seguidoId).orElseThrow();
        followRepo.deleteBySeguidorAndSeguido(seguidor, seguido);
    }
}