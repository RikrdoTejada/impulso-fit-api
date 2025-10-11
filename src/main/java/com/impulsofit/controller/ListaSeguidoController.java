package com.impulsofit.controller;

import com.impulsofit.model.ListaSeguido;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.ListaSeguidoRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ListaSeguidoController {

    private final ListaSeguidoRepository listaSeguidoRepository;
    private final UsuarioRepository usuarioRepository;

    public ListaSeguidoController(ListaSeguidoRepository listaSeguidoRepository,
                                  UsuarioRepository usuarioRepository) {
        this.listaSeguidoRepository = listaSeguidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/seguimientos")
    public ResponseEntity<?> crearSeguimiento(@RequestBody ListaSeguido listaSeguido) {
        long idUsuario = listaSeguido.getIdUsuario();    // Ya es Integer
        long idSeguido = listaSeguido.getIdSeguido();    // Ya es Integer

        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con id " + idUsuario);
        }
        if (!usuarioRepository.existsById(idSeguido)) {
            throw new RuntimeException("Seguido no encontrado con id " + idSeguido);
        }

        listaSeguido.setFecha(LocalDateTime.now());
        listaSeguidoRepository.save(listaSeguido);
        return ResponseEntity.ok("Seguimiento creado correctamente");
    }

    // Para listar todos los seguimientos
    @GetMapping("/seguimientos")
    public List<ListaSeguido> listarSeguimientos() {
        return listaSeguidoRepository.findAll();
    }
}