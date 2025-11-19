package com.impulsofit.dto.request;

public record AddProgresoRequestDTO (
    Integer horas,
    Integer minutos,
    Double kilometros,
    Double metros,
    Double cantidad,
    Integer series,
    Integer sesiones,
    Integer puntos,
    Integer dias,
    Double kilogramos
){}
