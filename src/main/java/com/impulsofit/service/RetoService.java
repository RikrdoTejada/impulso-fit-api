package com.impulsofit.service;

import com.impulsofit.model.Reto;
import com.impulsofit.repository.RetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RetoService {

    @Autowired
    private RetoRepository retoRepository;

    public Optional<Reto> obtenerPorId(Long id) {
        return retoRepository.findById(id);
    }
}