package com.impulsofit.dto.response;

import java.util.List;

public class BusquedaResponseDTO {
    private List<GrupoResponseDTO> grupos;
    private List<UsuarioResponseDTO> usuarios;
    private List<RetoResponseDTO> retos;

    public BusquedaResponseDTO() {}

    public BusquedaResponseDTO(List<GrupoResponseDTO> grupos, List<UsuarioResponseDTO> usuarios, List<RetoResponseDTO> retos) {
        this.grupos = grupos;
        this.usuarios = usuarios;
        this.retos = retos;
    }

    public List<GrupoResponseDTO> getGrupos() { return grupos; }
    public List<UsuarioResponseDTO> getUsuarios() { return usuarios; }
    public List<RetoResponseDTO> getRetos() { return retos; }
}
