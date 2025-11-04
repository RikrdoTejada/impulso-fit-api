package com.impulsofit.service;

import com.impulsofit.dto.request.RetoRequestDTO;
import com.impulsofit.dto.response.RetoResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.*;
import com.impulsofit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadRepository unidadRepository;
    private final RetoRepository retoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;


    @Transactional
    public RetoResponseDTO create(RetoRequestDTO reto){

        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + reto.id_usuario_creador()));

        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + reto.id_grupo()));

        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la unidad con el id: " + reto.id_unidad()));

        if(reto.descripcion()==null){
            throw new BusinessRuleException("La descripción no puede estar vacía.");
        }
        if(reto.titulo()==null || reto.titulo().length()<5){
            throw new BusinessRuleException("El título debe tener al menos 5 caracteres. Longitud actual: " + (reto.titulo()==null?0:reto.titulo().length()));
        }
        if (retoRepository.existsByTituloIgnoreCaseAndGrupo_IdGrupo(reto.titulo(), reto.id_grupo())) {
            throw new AlreadyExistsException("Ya existe un reto con el título: " + reto.titulo());
        }
        if(!membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(reto.id_usuario_creador(), reto.id_grupo())) {
            throw new ResourceNotFoundException("El usuario con el id: " + reto.id_usuario_creador() + " no pertenece al grupo con el id: " + reto.id_grupo());
        }
        if (reto.fecha_inicio().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(
                    "La fecha de inicio no puede ser anterior a la fecha actual. " +
                            "Fecha ingresada: " + reto.fecha_inicio()
            );
        }
        if(reto.fecha_fin().isBefore(LocalDate.now())){
            throw new BusinessRuleException(
                    "La fecha de fin no puede ser anterior a la fecha actual. " +
                            "Fecha ingresada: " + reto.fecha_fin());
        }

        Reto retoEntity = new Reto();
        retoEntity.setGrupo(grupo);
        retoEntity.setCreador(usuario);
        retoEntity.setUnidad(unidad);
        retoEntity.setDescripcion(reto.descripcion());
        retoEntity.setTitulo(reto.titulo());
        retoEntity.setObjetivoTotal(reto.objetivo_total());
        retoEntity.setFechaInicio(reto.fecha_inicio());
        retoEntity.setFechaFin(reto.fecha_fin());

        Reto saved = retoRepository.save(retoEntity);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RetoResponseDTO> findAll() {
        return retoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RetoResponseDTO update(Long id, RetoRequestDTO reto) {
        Reto retoEntity = retoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reto con el id: " + id));
        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + reto.id_usuario_creador()));
        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + reto.id_grupo()));
        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la unidad con el id: " + reto.id_unidad()));

        if(reto.descripcion()==null){
            throw new BusinessRuleException("La descripción no puede estar vacía.");
        }
        if(reto.titulo()==null || reto.titulo().length()<5){
            throw new BusinessRuleException("El título debe tener al menos 5 caracteres. Longitud actual: " + (reto.titulo()==null?0:reto.titulo().length()));
        }
        if(!membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(reto.id_usuario_creador(), reto.id_grupo())) {
            throw new ResourceNotFoundException("El usuario con el id: " + reto.id_usuario_creador() + " no pertenece al grupo con el id: " + reto.id_grupo());
        }
        if (retoRepository.existsByTituloIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(reto.titulo(), reto.id_grupo(), id)) {
            throw new AlreadyExistsException("Ya existe un reto con el título: "  + reto.titulo());
        }
        if (reto.fecha_inicio().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(
                    "La fecha de inicio no puede ser anterior a la fecha actual. " +
                            "Fecha ingresada: " + reto.fecha_inicio()
            );
        }
        if(reto.fecha_fin().isBefore(LocalDate.now())){
            throw new BusinessRuleException(
                    "La fecha de fin no puede ser anterior a la fecha actual. " +
                            "Fecha ingresada: " + reto.fecha_fin());
        }

        retoEntity.setIdReto(id);
        retoEntity.setGrupo(grupo);
        retoEntity.setCreador(usuario);
        retoEntity.setUnidad(unidad);
        retoEntity.setTitulo(reto.titulo());
        retoEntity.setDescripcion(reto.descripcion());
        retoEntity.setObjetivoTotal(reto.objetivo_total());
        retoEntity.setFechaInicio(reto.fecha_inicio());
        retoEntity.setFechaFin(reto.fecha_fin());

        Reto updated = retoRepository.save(retoEntity);

        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<RetoResponseDTO> findByGrupo_Id_grupo(Long id_grupo) {
        return retoRepository.findAllByGrupo_IdGrupo(id_grupo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RetoResponseDTO> findByCreador_Id(Long usuario_creador) {
        return retoRepository.findAllByCreador_IdUsuario(usuario_creador)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RetoResponseDTO> findByUnidad_IdUnidad(Long id_unidad) {
        return retoRepository.findAllByUnidad_IdUnidad(id_unidad)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!retoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el reto con el id: " + id);
        }
        retoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Reto> obtenerPorId(Long id) {
        return retoRepository.findById(id);
    }

    private RetoResponseDTO mapToResponse(Reto reto) {
        return new RetoResponseDTO(
                reto.getIdReto(),
                reto.getGrupo() != null ? reto.getGrupo().getNombre() : null,
                reto.getCreador() != null ? reto.getCreador().getNombres() : null,
                reto.getUnidad() != null ? reto.getUnidad().getNombre() : null,
                reto.getTitulo(),
                reto.getDescripcion(),
                reto.getObjetivoTotal(),
                reto.getFechaPublicacion(),
                reto.getFechaInicio(),
                reto.getFechaFin()
        );
    }
}