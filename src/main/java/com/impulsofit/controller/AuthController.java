package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.RegisterRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody RegisterRequestDTO u) {
        UsuarioResponseDTO saved = authService.register(u);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return authService.login(loginDTO);
    }

    @PutMapping("/recover")
    public ResponseEntity<UsuarioResponseDTO> updateCred(@RequestBody RecoverRequestDTO r) {
        return ResponseEntity.ok(authService.recoverCred(r));
    }
}