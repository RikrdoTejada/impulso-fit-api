package com.impulsofit.controller;

import com.impulsofit.model.Usuario;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("auth")
public class LoginController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public String login(@RequestBody Usuario loginData) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(loginData.getEmail());

        if (usuario.isPresent() && usuario.get().getContrasena().equals(loginData.getContrasena())) {
            return "Inicio de sesión exitoso: " + usuario.get().getNombre();
        } else {
            return "Credenciales incorrectas";
        }
    }
}
