package com.impulsofit.service;

import com.impulsofit.model.Unidad;
import org.springframework.stereotype.Service;

@Service
public class UnidadConverterService {

    public UnidadConverterService() {}

    public Double convertirAUnidadBase(Unidad unidad,
                                       Integer horas,
                                       Integer minutos,
                                       Double kilometros,
                                       Double metros,
                                       Double cantidad) {
        if (unidad == null) return null;
        String nombre = unidad.getNombre() == null ? "" : unidad.getNombre().toLowerCase();
        // Tiempo -> base: minutos
        if (nombre.contains("tiempo")) {
            if (horas != null || minutos != null) {
                int h = horas == null ? 0 : horas;
                int m = minutos == null ? 0 : minutos;
                return (double) (h * 60 + m);
            }
            return cantidad;
        }
        // Distancia -> base: kilometros
        if (nombre.contains("distancia")) {
            if (kilometros != null || metros != null) {
                double km = kilometros == null ? 0.0 : kilometros;
                double m = metros == null ? 0.0 : metros;
                return km + (m / 1000.0);
            }
            return cantidad;
        }
        // Otros
        if (cantidad != null) return cantidad;
        return null;
    }

    public int[] minutosAhorasYMinutos(double minutosTotales) {
        int horas = (int) (minutosTotales / 60);
        int minutos = (int) Math.round(minutosTotales % 60);
        return new int[]{horas, minutos};
    }

    public double[] kilometrosAPartes(double kmTotal) {
        int km = (int) Math.floor(kmTotal);
        int metros = (int) Math.round((kmTotal - km) * 1000);
        return new double[]{km, metros};
    }
}
