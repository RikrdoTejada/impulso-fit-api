package com.impulsofit.service;

import com.impulsofit.dto.response.PerfilResponseDTO;
import com.impulsofit.dto.response.SeguidoResponseDTO;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Seguido;
import com.impulsofit.model.Seguido.SeguidoId;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.SeguidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SeguidoService {

    private final PerfilRepository perfilRepository;
    private final SeguidoRepository seguidoRepository;

    @Transactional
    public SeguidoResponseDTO alternarSeguir(Long idPerfilSeguidor, Long idPerfilSeguido) {
        if (idPerfilSeguidor.equals(idPerfilSeguido)) {
            throw new BusinessRuleException("No puedes seguirte a ti mismo");
        }

        // Comprobar existencia del perfil seguidor
        perfilRepository.findById(idPerfilSeguidor)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil seguidor no encontrado: " + idPerfilSeguidor));

        Perfil perfilSeguido = perfilRepository.findById(idPerfilSeguido)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil a seguir no encontrado: " + idPerfilSeguido));

        String nombreMostrar = perfilSeguido.getNombrePerfil();
        if (nombreMostrar == null || nombreMostrar.trim().isEmpty()) {
            if (perfilSeguido.getPersona() != null) {
                nombreMostrar = perfilSeguido.getPersona().getNombres();
            } else {
                nombreMostrar = "el usuario";
            }
        }

        boolean ya = seguidoRepository.existsById_IdSeguidorAndId_IdSeguido(idPerfilSeguidor, idPerfilSeguido);
        if (ya) {
            seguidoRepository.deleteById_IdSeguidorAndId_IdSeguido(idPerfilSeguidor, idPerfilSeguido);
            return new SeguidoResponseDTO(false, "Dejaste de seguir a " + nombreMostrar);
        } else {
            SeguidoId id = new SeguidoId(idPerfilSeguido, idPerfilSeguidor);
            Seguido s = new Seguido();
            s.setId(id);
            s.setFechaSeguido(LocalDateTime.now());
            seguidoRepository.save(s);
            return new SeguidoResponseDTO(true, "Ahora sigues a " + nombreMostrar);
        }
    }

    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> listarSeguidos(Long idPerfilSeguidor) {
        List<Seguido> rows = seguidoRepository.findAllById_IdSeguidorOrderByFechaSeguidoDesc(idPerfilSeguidor);
        List<Long> ids = rows.stream().map(r -> r.getId().getIdSeguido()).collect(Collectors.toList());
        if (ids.isEmpty()) return List.of();
        List<Perfil> perfiles = perfilRepository.findAllById(ids);
        Map<Long, Perfil> map = perfiles.stream().collect(Collectors.toMap(Perfil::getIdPerfil, p -> p));
        return ids.stream().map(id -> {
            Perfil p = map.get(id);
            return new PerfilResponseDTO(p.getIdPerfil(), p.getPersona() != null ? p.getPersona().getNombres() : null, p.getPersona() != null ? p.getPersona().getGenero() : null);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> listarSeguidores(Long idPerfilSeguido) {
        List<Seguido> rows = seguidoRepository.findAllById_IdSeguidoOrderByFechaSeguidoDesc(idPerfilSeguido);
        List<Long> ids = rows.stream().map(r -> r.getId().getIdSeguidor()).collect(Collectors.toList());
        if (ids.isEmpty()) return List.of();
        List<Perfil> perfiles = perfilRepository.findAllById(ids);
        Map<Long, Perfil> map = perfiles.stream().collect(Collectors.toMap(Perfil::getIdPerfil, p -> p));
        return ids.stream().map(id -> {
            Perfil p = map.get(id);
            return new PerfilResponseDTO(p.getIdPerfil(), p.getPersona() != null ? p.getPersona().getNombres() : null, p.getPersona() != null ? p.getPersona().getGenero() : null);
        }).collect(Collectors.toList());
    }
}
