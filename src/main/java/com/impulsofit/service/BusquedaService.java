package com.impulsofit.service;

import com.impulsofit.dto.response.GrupoResponseDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.dto.response.UsuarioResponseDTO;
import com.impulsofit.dto.response.BusquedaResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Perfil;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.repository.PerfilRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BusquedaService {

    private final GrupoRepository grupoRepository;
    private final PerfilRepository perfilRepository;
    private final RetoRepository retoRepository;

    public BusquedaService(GrupoRepository grupoRepository, PerfilRepository perfilRepository, RetoRepository retoRepository) {
        this.grupoRepository = grupoRepository;
        this.perfilRepository = perfilRepository;
        this.retoRepository = retoRepository;
    }

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

        if (deporteId != null) {
            if (term.isEmpty()) {
                // Solo deporteId, sin término: buscar grupos y retos, NO perfiles
                grupos = grupoRepository.findByDeporte_IdDeporte(deporteId);
                retos = retoRepository.findByDeporteId(deporteId);
                perfiles = Collections.emptyList();  // ✅ NO buscar perfiles
            } else {
                // Hay deporteId Y término: buscar con ambos filtros
                grupos = grupoRepository.buscarPorNombreODeporteYDeporteId(term, deporteId);
                retos = retoRepository.searchByTermAndDeporteId(term, deporteId);
                perfiles = perfilRepository.searchByNombreApellido(term);  // ✅ Buscar perfiles solo si hay término
            }
        } else {
            // Solo término, sin deporteId: buscar entre todo
            grupos = grupoRepository.buscarPorNombreODeporte(term);
            retos = retoRepository.searchByTerm(term);
            perfiles = perfilRepository.searchByNombreApellido(term);
        }

        List<GrupoResponseDTO> gruposDto = grupos.stream()
                .map(g -> new GrupoResponseDTO(g.getIdGrupo(), g.getNombre(), g.getDeporte() != null ? g.getDeporte().getNombre() : null, g.getDescripcion(), "/grupos/" + g.getIdGrupo() + "/unirse"))
                .collect(Collectors.toList());

        List<UsuarioResponseDTO> usuariosDto = perfiles.stream()
                .map(p -> new UsuarioResponseDTO(
                        p.getIdPerfil(),
                        (p.getNombre() + " " + (p.getApellido() != null ? p.getApellido() : "")).trim(),
                        "/perfiles/" + p.getIdPerfil()
                ))
                .collect(Collectors.toList());

        List<RetoResponseDTO> retosDto = retos.stream()
                .map(r -> new RetoResponseDTO(r.getIdReto(), r.getTitulo(), r.getDescripcion(), "/retos/" + r.getIdReto()))
                .collect(Collectors.toList());

        return new BusquedaResponseDTO(gruposDto, usuariosDto, retosDto);
    }
}