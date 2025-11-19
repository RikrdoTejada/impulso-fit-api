package com.impulsofit.service;

import com.impulsofit.dto.response.*;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusquedaService {

    private final GrupoRepository grupoRepository;
    private final PerfilRepository perfilRepository;
    private final RetoRepository retoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final ParticipacionRetoRepository participacionRetoRepository;

    private void validarTermino(String termino, Integer deporteId) {
        if ((termino == null || termino.trim().isEmpty()) && deporteId == null) {
            throw new BusinessRuleException("Término de búsqueda no válido: debe contener al menos 1 carácter o seleccionar un deporte");
        }
        if (termino != null && !termino.trim().isEmpty() && !termino.matches("^[A-Za-z0-9 ]+$")) {
            throw new BusinessRuleException("Término de búsqueda no válido: sólo se permiten letras, números y espacios");
        }
    }

    @Transactional(readOnly = true)
    public BusquedaResponseDTO buscar(String termino, Integer deporteId) {
        validarTermino(termino, deporteId);
        String term = (termino == null) ? "" : termino.trim();

        List<Grupo> grupos;
        List<Reto> retos;
        List<Perfil> perfiles;

        Long deporteIdLong = (deporteId == null) ? null : deporteId.longValue();

        if (deporteId != null) {
            if (term.isEmpty()) {
                grupos   = grupoRepository.findByDeporte_IdDeporte(deporteIdLong);
                retos    = retoRepository.findByDeporteId(deporteIdLong);
                perfiles = Collections.emptyList();
            } else {
                grupos   = grupoRepository.buscarPorNombreODeporteYDeporteId(term, deporteIdLong);
                retos    = retoRepository.searchByTermAndDeporteId(term, deporteIdLong);
                perfiles = perfilRepository.searchByNombreApellido(term);
            }
        } else {
            grupos   = grupoRepository.buscarPorNombreODeporte(term);
            retos    = retoRepository.searchByTerm(term);
            perfiles = perfilRepository.searchByNombreApellido(term);
        }

        // Por si repo devolviera null
        grupos   = (grupos   == null) ? Collections.emptyList() : grupos;
        retos    = (retos    == null) ? Collections.emptyList() : retos;
        perfiles = (perfiles == null) ? Collections.emptyList() : perfiles;

        List<GrupoResponseDTO> gruposDto = grupos.stream()
                .map(g -> new GrupoResponseDTO(
                        g.getId(),
                        g.getNombre(),
                        g.getDeporte() != null ? g.getDeporte().getNombre() : null,
                        g.getDescripcion(),
                        "/grupos/" + g.getId() + "/unirse",
                        g.getUbicacion(),
                        g.getFechaCreacion() == null ? null : g.getFechaCreacion().toLocalDate(),
                        membresiaGrupoRepository.countByGrupo_IdGrupo(g.getId())
                ))
                .toList();

        List<PerfilResponseDTO> perfilesDto = perfiles.stream()
                .map(p -> new PerfilResponseDTO(
                        p.getIdPerfil(),
                        p.getPersona().getNombres(),
                        p.getPersona().getGenero()
                ))
                .toList();

        List<RetoResponseDTO> retosDto = retos.stream()
                .map(r -> new RetoResponseDTO(
                        r.getIdReto(),
                        r.getGrupo() != null ? r.getGrupo().getNombre() : null,
                        r.getPerfilCreador() != null ? r.getPerfilCreador().getPersona().getNombres() : null,
                        r.getUnidad() != null ? r.getUnidad().getNombre() : null,
                        r.getTitulo(),
                        r.getDescripcion(),
                        r.getObjetivoTotal(),
                        r.getFechaPublicacion(),
                        r.getFechaInicio(),
                        r.getFechaFin(),
                        participacionRetoRepository.countByIdReto(r.getIdReto())
                ))
                .toList();

        return new BusquedaResponseDTO(gruposDto, perfilesDto, retosDto);
    }
}
