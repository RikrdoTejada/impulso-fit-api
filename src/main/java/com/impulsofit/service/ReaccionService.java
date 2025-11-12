package com.impulsofit.service;

import com.impulsofit.dto.request.ReaccionRequestDTO;
import com.impulsofit.dto.response.ReaccionResponseDTO;
import com.impulsofit.exception.AlreadyExistsException;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Publicacion;
import com.impulsofit.model.Reaccion;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.PublicacionRepository;
import com.impulsofit.repository.ReaccionRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor()
public class ReaccionService {
    private final ReaccionRepository reaccionRepository;
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    @Transactional
    public ReaccionResponseDTO create(ReaccionRequestDTO reaccion) {
        Perfil perfil = perfilRepository.findById(reaccion.id_perfil())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado"));

        Publicacion publicacion = publicacionRepository.findById(reaccion.id_publicacion())
                .orElseThrow(() -> new ResourceNotFoundException("Publicacion no encontrada"));

        //Solo se puede reaccionar una vez a una publicacion
        if (reaccionRepository.existsReaccionByPerfilAndPublicacion(perfil, publicacion)) {
            throw new AlreadyExistsException("El usuario ya reaccionó a esta publicacion");
        }
        Reaccion reaccionEntity = new Reaccion();
        reaccionEntity.setPerfil(perfil);
        reaccionEntity.setPublicacion(publicacion);

        Reaccion saved = reaccionRepository.save(reaccionEntity);

        return new ReaccionResponseDTO(
                saved.getIdReaccion(),
                saved.getPerfil().getIdPerfil(),
                saved.getPublicacion().getIdPublicacion(),
                saved.getFechaRegistro()
        );
    }

    @Transactional
    public void delete(Long id) {
        if (!reaccionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reaccion no existe con el id: " + id);
        }
        reaccionRepository.deleteById(id);
    }


}
