package com.impulsofit.repository;

import com.impulsofit.model.Deporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeporteRepository extends JpaRepository<Deporte, Long> {
    // Puedes agregar métodos personalizados si los necesitas
    Deporte findByNombre(String nombre);
}