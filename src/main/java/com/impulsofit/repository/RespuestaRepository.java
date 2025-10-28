package com.impulsofit.repository;

import com.impulsofit.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespuestaRepository extends JpaRepository<Respuesta,Long> {

    Respuesta findByUsuario_IdUsuario(Long id);
}
