package com.impulsofit.repository;

import com.impulsofit.model.ListaSeguido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ListaSeguidoRepository extends JpaRepository<ListaSeguido, Long> {

    @Query("SELECT l.idSeguido FROM ListaSeguido l WHERE l.idUsuario = :idUsuario")
    List<Long> findIdsSeguidosByUsuario(Long idUsuario);
}
