package com.impulsofit.dto.request;

public class ComentarioRequestDTO {
    private String contenido;
    private Long usuarioId;
    private Long publicacionId;

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public Long getPublicacionId() { return publicacionId; }
    public void setPublicacionId(Long publicacionId) { this.publicacionId = publicacionId; }
}
