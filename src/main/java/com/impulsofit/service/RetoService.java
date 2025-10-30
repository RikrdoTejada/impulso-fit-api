package com.impulsofit.service;

import com.impulsofit.dto.request.RetoRequest;
import com.impulsofit.dto.response.RetoResponse;
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
    public RetoResponse create(RetoRequest reto){

        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + reto.id_usuario_creador()));

        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + reto.id_grupo()));

        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la unidad con el id: " + reto.id_unidad()));

        //descripcion obligatoria
        if(reto.descripcion()==null){
            throw new BusinessRuleException("La descripcion no puede estar vacia.");
        }
        //longitud minima de nombre es 5 caracteres
        if(reto.nombre().length()<5){
            throw new BusinessRuleException("El nombre debe tener al menos 5 caracteres. Longitud actual: " +reto.nombre().length());
        }
        //Unicidad de reto por grupo
        if (retoRepository.existsByNombreIgnoreCaseAndGrupo_IdGrupo(reto.nombre(), reto.id_grupo())) {
            throw new AlreadyExistsException("Ya existe un reto con el nombre: " + reto.nombre());
        }
        //Pertenencia a Grupo
        if(!membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(reto.id_usuario_creador(), reto.id_grupo())) {
            throw new ResourceNotFoundException("El usuario con el id: " + reto.id_usuario_creador() + " no pertenece al grupo con el id: " + reto.id_grupo());
        }
        //Fecha de inicio o fin de reto no puede ser en el pasado
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
        retoEntity.setNombre(reto.nombre());
        retoEntity.setDescripcion(reto.descripcion());
        retoEntity.setFechaInicio(reto.fecha_inicio());
        retoEntity.setFechaFin(reto.fecha_fin());
        retoEntity.setObjetivo(reto.objetivo());

        Reto saved = retoRepository.save(retoEntity);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RetoResponse> findAll() {
        return retoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RetoResponse update(Long id, RetoRequest reto) {
        //Validaciones
        //Existencia de reto
        Reto retoEntity = retoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reto con el id: " + id));
        //Existencia de usuario
        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + reto.id_usuario_creador()));
        //Existencia de grupo
        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + reto.id_grupo()));
        //Existencia de unidad
        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la unidad con el id: " + reto.id_unidad()));
        //Regla de negocio: Descripcion no puede estar vacía o ser menor a 5 caracteres
        if(reto.descripcion()==null){
            throw new BusinessRuleException("La descripción no puede estar vacia.");
        }
        if(reto.descripcion().length()<5){
            throw new BusinessRuleException("El nombre debe tener al menos 5 caracteres. Longitud actual: "
                    +reto.nombre().length());
        }
        //Pertenencia a Grupo
        if(!membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(reto.id_usuario_creador(), reto.id_grupo())) {
            throw new ResourceNotFoundException("El usuario con el id: " + reto.id_usuario_creador() + " no pertenece al grupo con el id: " + reto.id_grupo());
        }
        //Unicidad de reto por grupo
        if (retoRepository.existsByNombreIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(reto.nombre(), reto.id_grupo(), id)) {
            throw new AlreadyExistsException("Ya existe un reto con el nombre: "  + reto.nombre());
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

        //sets
        retoEntity.setIdReto(id);
        retoEntity.setGrupo(grupo);
        retoEntity.setCreador(usuario);
        retoEntity.setUnidad(unidad);
        retoEntity.setNombre(reto.nombre());
        retoEntity.setDescripcion(reto.descripcion());
        retoEntity.setObjetivo(reto.objetivo());
        retoEntity.setFechaInicio(reto.fecha_inicio());
        retoEntity.setFechaFin(reto.fecha_fin());

        Reto updated = retoRepository.save(retoEntity);

        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<RetoResponse> findByGrupo_Id_grupo(Long id_grupo) {
       return retoRepository.findAllByGrupo_IdGrupo(id_grupo)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RetoResponse> findByCreador_Id(Long usuario_creador) {
        return retoRepository.findAllByCreador_IdUsuario(usuario_creador)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RetoResponse> findByUnidad_IdUnidad(Long id_unidad) {
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

    private RetoResponse mapToResponse(Reto reto) {
        return new RetoResponse(
                reto.getIdReto(),
                reto.getGrupo().getNombre(),
                reto.getCreador().getNombres(),
                reto.getUnidad().getNombre(),
                reto.getNombre(),
                reto.getDescripcion(),
                reto.getObjetivo(),
                reto.getFechaPublicacion(),
                reto.getFechaInicio(),
                reto.getFechaFin()
        );
    }

}

