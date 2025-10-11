package com.impulsofit.repository;
import com.impulsofit.model.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PublicacionRepository extends JpaRepository<Publicacion,Long> {
}
