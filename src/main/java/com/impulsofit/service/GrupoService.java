package com.impulsofit.service;

import com.impulsofit.model.Grupo;
import com.impulsofit.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    // Listar todos los grupos
    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    // Buscar grupos por nombre o deporte
    public List<Grupo> buscarPorNombreODeporte(String filtro) {
        return grupoRepository.buscarPorNombreODeporte(filtro);
    }
}
