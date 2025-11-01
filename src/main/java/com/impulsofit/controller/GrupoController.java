package com.impulsofit.controller;

import com.impulsofit.dto.response.GrupoPopularDTO;
import com.impulsofit.model.Deporte;
import com.impulsofit.model.Grupo;
import com.impulsofit.repository.DeporteRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.service.GrupoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GrupoController {

    private final GrupoService grupoService;
    private final GrupoRepository grupoRepository;
    private final DeporteRepository deporteRepository; // ✅ Agregar esto

    public GrupoController(GrupoService grupoService,
                           GrupoRepository grupoRepository,
                           DeporteRepository deporteRepository) { // ✅ Actualizar constructor
        this.grupoService = grupoService;
        this.grupoRepository = grupoRepository;
        this.deporteRepository = deporteRepository;
    }

    // Grupos populares para todos
    @GetMapping("/grupos/populares")
    public List<GrupoPopularDTO> obtenerGruposPopulares() {
        return grupoService.obtenerGruposPopulares();
    }

    // Grupos populares recomendados para un usuario específico
    @GetMapping("/grupos/populares/{idUsuario}")
    public List<GrupoPopularDTO> obtenerGruposPopularesParaUsuario(@PathVariable Long idUsuario) {
        return grupoService.obtenerGruposPopularesParaUsuario(idUsuario);
    }

    // ✅ Endpoint para crear deportes de prueba
    @PostMapping("/deportes-test")
    public String crearDeportesDePrueba() {
        try {
            // Crear deportes de prueba
            Deporte deporte1 = new Deporte("Fitness", "Fuerza");
            Deporte deporte2 = new Deporte("Running", "Cardio");
            Deporte deporte3 = new Deporte("Yoga", "Flexibilidad");
            Deporte deporte4 = new Deporte("CrossFit", "Fuerza");
            Deporte deporte5 = new Deporte("Natación", "Cardio");

            deporteRepository.save(deporte1);
            deporteRepository.save(deporte2);
            deporteRepository.save(deporte3);
            deporteRepository.save(deporte4);
            deporteRepository.save(deporte5);

            return "5 deportes de prueba creados exitosamente. IDs: " +
                    deporte1.getIdDeporte() + ", " + deporte2.getIdDeporte() + ", " +
                    deporte3.getIdDeporte() + ", " + deporte4.getIdDeporte() + ", " +
                    deporte5.getIdDeporte();

        } catch (Exception e) {
            return "Error creando deportes: " + e.getMessage();
        }
    }

    // Listar todos los deportes
    @GetMapping("/deportes")
    public List<Deporte> listarDeportes() {
        return deporteRepository.findAll();
    }

    // Listar todos los grupos
    @GetMapping("/grupos")
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    // ✅ Endpoint para crear grupos de prueba
    @PostMapping("/grupos-test")
    public String crearGruposDePrueba() {
        try {
            // Primero verificar si existen deportes
            List<Deporte> deportes = deporteRepository.findAll();
            if (deportes.isEmpty()) {
                return "Error: No hay deportes en la base de datos. Ejecuta primero /deportes-test";
            }

            Long idUsuarioCreador = 28L;
            Long idDeporte = deportes.get(0).getIdDeporte(); // Usar el primer deporte

            // Crear grupos de prueba
            Grupo grupo1 = new Grupo();
            grupo1.setIdUsuarioCreador(idUsuarioCreador);
            grupo1.setIdDeporte(idDeporte);
            grupo1.setNombre("Fitness Beginners");
            grupo1.setDescripcion("Comunidad para principiantes en el mundo del fitness");
            grupo1.setUbicacion("Virtual");
            grupo1.setFotoGrupo("fitness-beginners.jpg");
            grupo1.setCantidadMiembros(150);

            Grupo grupo2 = new Grupo();
            grupo2.setIdUsuarioCreador(idUsuarioCreador);
            grupo2.setIdDeporte(idDeporte);
            grupo2.setNombre("Running Lovers");
            grupo2.setDescripcion("Amantes del running y las carreras");
            grupo2.setUbicacion("Virtual");
            grupo2.setFotoGrupo("running-lovers.jpg");
            grupo2.setCantidadMiembros(89);

            Grupo grupo3 = new Grupo();
            grupo3.setIdUsuarioCreador(idUsuarioCreador);
            grupo3.setIdDeporte(idDeporte);
            grupo3.setNombre("Yoga & Mindfulness");
            grupo3.setDescripcion("Espacio para practicantes de yoga y meditación");
            grupo3.setUbicacion("Virtual");
            grupo3.setFotoGrupo("yoga-mindfulness.jpg");
            grupo3.setCantidadMiembros(64);

            Grupo grupo4 = new Grupo();
            grupo4.setIdUsuarioCreador(idUsuarioCreador);
            grupo4.setIdDeporte(idDeporte);
            grupo4.setNombre("CrossFit Warriors");
            grupo4.setDescripcion("Comunidad de CrossFit y entrenamiento funcional");
            grupo4.setUbicacion("Virtual");
            grupo4.setFotoGrupo("crossfit-warriors.jpg");
            grupo4.setCantidadMiembros(42);

            Grupo grupo5 = new Grupo();
            grupo5.setIdUsuarioCreador(idUsuarioCreador);
            grupo5.setIdDeporte(idDeporte);
            grupo5.setNombre("Nutrición Saludable");
            grupo5.setDescripcion("Consejos y recetas para una alimentación balanceada");
            grupo5.setUbicacion("Virtual");
            grupo5.setFotoGrupo("nutricion-saludable.jpg");
            grupo5.setCantidadMiembros(203);

            grupoRepository.save(grupo1);
            grupoRepository.save(grupo2);
            grupoRepository.save(grupo3);
            grupoRepository.save(grupo4);
            grupoRepository.save(grupo5);

            return "5 grupos de prueba creados exitosamente";

        } catch (Exception e) {
            return "Error creando grupos: " + e.getMessage();
        }
    }
}