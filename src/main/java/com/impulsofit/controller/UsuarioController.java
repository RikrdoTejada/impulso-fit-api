package com.impulsofit.controller;

import com.impulsofit.dto.request.RecoverRequestDTO;
import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


@GetMapping
public List<UsuarioResponseDTO> list() {
    return service.list();
}
    }

@PutMapping("/update/cred/{id}")
public ResponseEntity<UsuarioResponseDTO> updateCred(@PathVariable Long id, @RequestBody RecoverRequestDTO r) {
    return ResponseEntity.ok(usuarioService.updateCred(id, r));
}

@PutMapping("/update/info/{id}")
public ResponseEntity<UsuarioResponseDTO> updateInfo(@PathVariable Long id, @RequestBody UsuarioRequestDTO u) {
    return ResponseEntity.ok(usuarioService.updateInfo(id, u));
}
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO update(@PathVariable Long id, @RequestBody UsuarioRequestDTO request) {
        return service.update(id, request);
    }

@DeleteMapping("{id}")
public ResponseEntity<Void> delete(@PathVariable Long id) {
    usuarioService.delete(id);
    return ResponseEntity.noContent().build();
}
    }
}