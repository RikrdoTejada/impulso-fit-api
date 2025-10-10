package com.impulsofit.service;

import com.impulsofit.model.Perfil;
import com.impulsofit.repository.PerfilRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    public Optional<Perfil> actualizarPerfil(Long idPerfil, Perfil datosActualizados) {
        return perfilRepository.findById(idPerfil).map(perfil -> {
            perfil.setNombre(datosActualizados.getNombre());
            perfil.setApellido(datosActualizados.getApellido());
            perfil.setBiografia(datosActualizados.getBiografia());
            perfil.setUbicacion(datosActualizados.getUbicacion());
            perfil.setFotoPerfil(datosActualizados.getFotoPerfil());
            return perfilRepository.save(perfil);
        });
    }
}
