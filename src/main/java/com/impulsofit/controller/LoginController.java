package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.LoginService;
import com.impulsofit.service.RecoverService;
import com.impulsofit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RecoverService  recoverService;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> create(@RequestBody UsuarioRequestDTO u) {
        UsuarioResponseDTO saved = usuarioService.create(u);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @PutMapping("/recover")
    public ResponseEntity<UsuarioResponseDTO> updateCred(@RequestBody RecoverRequestDTO r) {
        return ResponseEntity.ok(recoverService.recoverCred(r));
    }
}