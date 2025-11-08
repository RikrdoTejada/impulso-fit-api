package com.impulsofit.unit;

import com.impulsofit.model.Unidad;
import com.impulsofit.service.UnidadConverterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("UnidadConverterService - Pruebas Unitarias")
class UnidadConverterServiceTest {

    @InjectMocks
    private UnidadConverterService converter;

    private Unidad distancia;
    private Unidad tiempo;
    private Unidad otra;

    @BeforeEach
    void setUp() {
        distancia = createUnidad("Distancia");
        tiempo = createUnidad("Tiempo");
        otra = createUnidad("Otro");
    }

    private Unidad createUnidad(String nombre) { Unidad u = new Unidad(); u.setNombre(nombre); return u; }

    @Test
    @DisplayName("Convertir distancia a km")
    void convertirAUnidadBase_distancia_shouldComputeKm() {
        Double val = converter.convertirAUnidadBase(distancia, null, null, 2.0, 500.0, null);
        assertThat(val).isEqualTo(2.5);
    }

    @Test
    @DisplayName("Convertir tiempo a minutos")
    void convertirAUnidadBase_tiempo_shouldComputeMinutes() {
        Double val = converter.convertirAUnidadBase(tiempo, 1, 30, null, null, null);
        assertThat(val).isEqualTo(90.0);
    }

    @Test
    @DisplayName("Unknown unit returns null")
    void convertirAUnidadBase_unknown_returnsCantidadOrNull() {
        Double val = converter.convertirAUnidadBase(otra, null, null, null, null, null);
        assertThat(val).isNull();
    }
}
