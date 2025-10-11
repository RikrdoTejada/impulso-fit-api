package com.impulsofit.controller;

import com.impulsofit.dto.request.CreateChallengeRequest;
import com.impulsofit.dto.response.ChallengeResponse;
import com.impulsofit.service.ChallengeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final ChallengeService service;

    public ChallengeController(ChallengeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ChallengeResponse> create(@Valid @RequestBody CreateChallengeRequest req) {
        ChallengeResponse response = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}