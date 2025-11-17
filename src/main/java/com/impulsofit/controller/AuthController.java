package com.impulsofit.controller;

import com.impulsofit.dto.request.DeleteRequestDTO;
import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.CredentialsRequestDTO;
import com.impulsofit.dto.request.RegisterRequestDTO;
import com.impulsofit.dto.response.AuthResponseDTO;
import com.impulsofit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO u) {
        AuthResponseDTO saved = authService.register(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        AuthResponseDTO saved = authService.login(loginDTO);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/recover")
    public ResponseEntity<AuthResponseDTO> updateCred(@Valid @RequestBody CredentialsRequestDTO r) {
        return ResponseEntity.ok(authService.recoverCred(r));
    }

    @DeleteMapping("/delete")
    public void deleteCred(@Valid @RequestBody DeleteRequestDTO d) { authService.delete(d);}
}