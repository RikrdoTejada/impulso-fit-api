package com.impulsofit.repository;

import com.impulsofit.model.ListaSeguido;
import com.impulsofit.model.ListaSeguidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ListaSeguidoRepository extends JpaRepository<ListaSeguido, ListaSeguidoId> {

    // CORREGIDO: Usar campos directos en lugar de relaciones
    @Query("SELECT l.idSeguido FROM ListaSeguido l WHERE l.idUsuario = :idUsuario")
    List<Long> findSeguidosIdsPorUsuario(@Param("idUsuario") Long idUsuario);

    // Método adicional útil
    //List<ListaSeguido> findByIdUsuario(long idUsuario);

    // Verificar si ya existe un seguimiento
    //boolean existsByIdUsuarioAndIdSeguido(long idUsuario, long idSeguido);
}