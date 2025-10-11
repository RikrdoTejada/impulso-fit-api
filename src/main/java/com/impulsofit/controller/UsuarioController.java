package com.impulsofit.controller;

import com.impulsofit.model.Usuario;
import com.impulsofit.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.impulsofit.dto.request.UsuarioDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;

@RestController
@RequestMapping("/users")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@RequestBody UsuarioDTO dto) {
        Usuario usuario = usuarioService.registrarUsuario(dto);

        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getEdad(),
                usuario.getGenero()
        );

        return ResponseEntity.ok(response);
    }
}
