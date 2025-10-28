package com.impulsofit.dto.request;

public class BusquedaRequestDTO {
    private String termino;

    public BusquedaRequestDTO() {}
    public BusquedaRequestDTO(String termino) { this.termino = termino; }
    public String getTermino() { return termino; }
    public void setTermino(String termino) { this.termino = termino; }
}

