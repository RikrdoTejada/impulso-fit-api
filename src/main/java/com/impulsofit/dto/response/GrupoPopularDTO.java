package com.impulsofit.dto.response;

public record GrupoPopularDTO (
    Long idGrupo,
    String nombre,
    String descripcion,
    String fotoGrupo,
    Integer cantidadMiembros
){

}
