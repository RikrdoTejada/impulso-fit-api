package com.impulsofit.controller;

import com.impulsofit.dto.request.LoginRequestDTO;
import com.impulsofit.dto.request.RecoverRequest;
import com.impulsofit.dto.response.LoginResponseDTO;
import com.impulsofit.dto.response.UsuarioResponse;
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
    private  UsuarioService usuarioService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginDTO) {
        return loginService.login(loginDTO);
    }

    @PutMapping("/recover")
    public ResponseEntity<UsuarioResponse> updateCred(@RequestBody RecoverRequest r) {
        return ResponseEntity.ok(usuarioService.updateCred(r));
    }
}