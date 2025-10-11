package com.impulsofit.service;

import com.impulsofit.dto.request.RetoRequest;
import com.impulsofit.dto.response.RetoResponse;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Reto;
import com.impulsofit.model.Usuario;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.Unidad;
import com.impulsofit.repository.RetoRepository;
import com.impulsofit.repository.UnidadRepository;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class RetoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadRepository unidadRepository;
    private final RetoRepository retoRepository;

    @Transactional
    public RetoResponse create(RetoRequest reto){

        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("Grupo no encontrado"));

        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada"));

        //descripcion obligatoria
        if(reto.descripcion()==null){
            throw new BusinessRuleException("La descripcion no puede estar vacio.");
        }
        //longitud minima de nombre es 5 caracteres
        if(reto.nombre().length()<5){
            throw new BusinessRuleException("El nombre debe tener al menos 5 caracteres. Longitud actual: " +reto.nombre().length());
        }

        Reto retoEntity = new Reto();
        retoEntity.setGrupo(grupo);
        retoEntity.setUsuario_creador(usuario);
        retoEntity.setUnidad(unidad);
        retoEntity.setNombre(reto.nombre());
        retoEntity.setDescripcion(reto.descripcion());
        retoEntity.setFecha_inicio(reto.fecha_inicio());
        retoEntity.setFecha_fin(reto.fecha_fin());
        retoEntity.setObjetivo(reto.objetivo());

        Reto saved = retoRepository.save(retoEntity);

        return new RetoResponse(
                saved.getId_reto(),
                saved.getGrupo().getNombre(),
                saved.getUsuario_creador().getNombre(),
                saved.getUnidad().getNombre(),
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getObjetivo(),
                saved.getFecha_inicio(),
                saved.getFecha_fin()
        );
    }

    @Transactional(readOnly = true)
    public List<Reto> findAll() {
        return retoRepository.findAll();
    }

    @Transactional
    public Reto update(Long id, Reto reto) {
        if (!retoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el reto con el id: " + id);
        }
        reto.setId_reto(id);
        return retoRepository.save(reto);
    }

    @Transactional
    public void delete(Long id) {
        if (!retoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el reto con el id: " + id);
        }
        retoRepository.deleteById(id);
    }
}
