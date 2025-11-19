package com.impulsofit.repository;

import com.impulsofit.model.Seguido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidoRepository extends JpaRepository<Seguido, Seguido.SeguidoId> {
    List<Seguido> findAllById_IdSeguidorOrderByFechaSeguidoDesc(Long idSeguidor);
    boolean existsById_IdSeguidorAndId_IdSeguido(Long idSeguidor, Long idSeguido);
    void deleteById_IdSeguidorAndId_IdSeguido(Long idSeguidor, Long idSeguido);
    List<Seguido> findAllById_IdSeguidoOrderByFechaSeguidoDesc(Long idSeguido);
}
