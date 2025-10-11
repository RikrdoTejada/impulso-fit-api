package com.impulsofit.controller;

import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PublicacionController {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;

    public PublicacionController(PublicacionRepository publicacionRepository,
                                 UsuarioRepository usuarioRepository) {
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Endpoint para crear publicaciones de prueba
    @PostMapping("/publicaciones-test")
    public ResponseEntity<?> crearPublicacionesTest() {
        try {
            // Obtener usuarios existentes (usando los IDs que tienes: 28, 39, 40, 42)
            Usuario usuario28 = usuarioRepository.findById(28L)
                    .orElseThrow(() -> new RuntimeException("Usuario 28 no encontrado"));
            Usuario usuario39 = usuarioRepository.findById(39L)
                    .orElseThrow(() -> new RuntimeException("Usuario 39 no encontrado"));
            Usuario usuario40 = usuarioRepository.findById(40L)
                    .orElseThrow(() -> new RuntimeException("Usuario 40 no encontrado"));

            // Crear publicaciones de prueba
            Publicacion publicacion1 = new Publicacion();
            publicacion1.setUsuario(usuario28);
            publicacion1.setContenido("¡Hola a todos! Estoy empezando mi journey fitness 💪");
            publicacion1.setFechaPublicacion(LocalDateTime.now().minusDays(2));
            publicacionRepository.save(publicacion1);

            Publicacion publicacion2 = new Publicacion();
            publicacion2.setUsuario(usuario39);
            publicacion2.setContenido("Hoy completé mi primer maratón! 🏃‍♂️🎉");
            publicacion2.setFechaPublicacion(LocalDateTime.now().minusDays(1));
            publicacionRepository.save(publicacion2);

            Publicacion publicacion3 = new Publicacion();
            publicacion3.setUsuario(usuario40);
            publicacion3.setContenido("Recomendaciones para rutina de gym? 🤔");
            publicacion3.setFechaPublicacion(LocalDateTime.now());
            publicacionRepository.save(publicacion3);

            return ResponseEntity.ok("3 publicaciones de prueba creadas exitosamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creando publicaciones: " + e.getMessage());
        }
    }

    // Endpoint opcional: listar todas las publicaciones
    @GetMapping("/publicaciones")
    public List<Publicacion> listarPublicaciones() {
        return publicacionRepository.findAll();
    }
}