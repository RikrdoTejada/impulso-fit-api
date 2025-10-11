package com.impulsofit.service;

import com.impulsofit.model.Grupo;
import com.impulsofit.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    public List<Grupo> listarGrupos() {
        return grupoRepository.findAll();
    }

    public Optional<Grupo> obtenerPorId(Long idGrupo) {
        return grupoRepository.findById(idGrupo);
    }

    public Grupo guardar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public void eliminar(Long idGrupo) {
        grupoRepository.deleteById(idGrupo);
    }
}
