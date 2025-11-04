package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.LoginService;
import com.impulsofit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @PutMapping("/recover")
    public ResponseEntity<UsuarioResponseDTO> updateCred(@PathVariable Long id, @RequestBody RecoverRequestDTO r) {
        return ResponseEntity.ok(usuarioService.updateCred(id, r));
    }
}