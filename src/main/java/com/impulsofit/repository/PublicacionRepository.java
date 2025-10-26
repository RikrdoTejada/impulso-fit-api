package com.impulsofit.repository;

import com.impulsofit.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {
}