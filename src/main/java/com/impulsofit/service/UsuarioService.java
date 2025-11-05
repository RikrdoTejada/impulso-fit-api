package com.impulsofit.service;

import com.impulsofit.dto.request.UsuarioRequestDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO create(UsuarioRequestDTO u);
    UsuarioResponseDTO update(Long id, UsuarioRequestDTO u);      // <-- NUEVO
    void delete(Long id);
    UsuarioResponseDTO getById(Long id);
    List<UsuarioResponseDTO> list();
}