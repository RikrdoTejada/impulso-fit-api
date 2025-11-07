package com.impulsofit.unit;

import com.impulsofit.model.Unidad;
import com.impulsofit.service.UnidadConverterService;
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

    @Test
    @DisplayName("Convertir distancia a km")
    void convertirAUnidadBase_distancia_shouldComputeKm() {
        Unidad u = new Unidad();
        u.setNombre("Distancia");

        Double val = converter.convertirAUnidadBase(u, null, null, 2.0, 500.0, null);
        assertThat(val).isEqualTo(2.5);
    }

    @Test
    @DisplayName("Convertir tiempo a minutos")
    void convertirAUnidadBase_tiempo_shouldComputeMinutes() {
        Unidad u = new Unidad();
        u.setNombre("Tiempo");

        Double val = converter.convertirAUnidadBase(u, 1, 30, null, null, null);
        assertThat(val).isEqualTo(90.0);
    }

    @Test
    @DisplayName("Unknown unit returns null")
    void convertirAUnidadBase_unknown_returnsCantidadOrNull() {
        Unidad u = new Unidad();
        u.setNombre("Otro");
        Double val = converter.convertirAUnidadBase(u, null, null, null, null, null);
        assertThat(val).isNull();
    }
}
