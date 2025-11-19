package com.impulsofit.service;

import com.impulsofit.model.Unidad;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UnidadConverterService {

    public Double convertirAUnidadBase(Unidad unidad,
                                       Integer horas,
                                       Integer minutos,
                                       Double kilometros,
                                       Double metros,
                                       Double cantidad) {
        if (unidad == null) return null;

        String nombre = unidad.getNombre() == null
                ? ""
                : unidad.getNombre().toLowerCase().trim();

        // ----- TIEMPO -> base: minutos -----
        // Soporta nombres que contengan "tiempo", "hora", "minut"
        if (nombre.contains("tiempo") || nombre.contains("hora") || nombre.contains("minut")) {
            int h = (horas == null) ? 0 : horas;
            int m = (minutos == null) ? 0 : minutos;
            double totalMin = h * 60 + m;

            // Si no enviaron horas/minutos pero sí cantidad, asumimos que cantidad ya viene en minutos
            if (totalMin == 0 && cantidad != null) {
                totalMin = cantidad;
            }

            return (totalMin > 0) ? totalMin : null;
        }

        // ----- DISTANCIA -> base: kilómetros -----
        // Soporta nombres que contengan "distancia", "km", "kilometro", "kilómetro"
        if (nombre.contains("distancia") || nombre.contains("km")
                || nombre.contains("kilometro") || nombre.contains("kilómetro")) {

            double km = (kilometros == null) ? 0.0 : kilometros;
            double m  = (metros == null) ? 0.0 : metros;
            double totalKm = km + (m / 1000.0);

            // Si no enviaron km/metros pero sí cantidad, asumimos que cantidad ya viene en kilómetros
            if (totalKm == 0.0 && cantidad != null) {
                totalKm = cantidad;
            }

            return (totalKm > 0.0) ? totalKm : null;
        }

        // ----- OTRAS UNIDADES (SESIONES, REPETICIONES, KG, ETC.) -----
        // Para lo demás, 'cantidad' como unidad base genérica.
        if (cantidad != null && cantidad > 0) {
            return cantidad;
        }

        // Si no hay nada válido,  null
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
