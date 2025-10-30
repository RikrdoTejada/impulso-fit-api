package com.impulsofit.dto.response;

public class RetoResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String detalleUrl;

    public RetoResponseDTO() {}
    public RetoResponseDTO(Long id, String titulo, String descripcion, String detalleUrl) {
        this.id = id; this.titulo = titulo; this.descripcion = descripcion; this.detalleUrl = detalleUrl;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getDetalleUrl() { return detalleUrl; }
}

