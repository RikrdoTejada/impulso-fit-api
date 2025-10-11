package com.impulsofit.controller;

import com.impulsofit.model.Challenge;
import com.impulsofit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/groups/{groupId}/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService service;

    // Visualizar retos por grupo
    @GetMapping
    public List<Challenge> listar(@PathVariable Long groupId) {
        return service.listarPorGrupo(groupId);
    }
}