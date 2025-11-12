package com.impulsofit.service;

import com.impulsofit.dto.request.MembresiaGrupoRequestDTO;
import com.impulsofit.dto.response.MembresiaGrupoResponseDTO;
import com.impulsofit.exception.ResourceNotFoundException;
import com.impulsofit.exception.BusinessRuleException;
import com.impulsofit.model.Grupo;
import com.impulsofit.model.MembresiaGrupo;
import com.impulsofit.model.Perfil;
import com.impulsofit.model.Usuario;
import com.impulsofit.repository.GrupoRepository;
import com.impulsofit.repository.MembresiaGrupoRepository;
import com.impulsofit.repository.PerfilRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class MembresiaGrupoService {
    private final MembresiaGrupoRepository membresiaGrupoRepository;
    private final GrupoRepository grupoRepository;
    private final PerfilRepository perfilRepository;

    public MembresiaGrupoResponseDTO create(MembresiaGrupoRequestDTO membresia) {
        Perfil perfil = perfilRepository.findById(membresia.id_perfil())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el perfil con el id: " + membresia.id_perfil()));

        Grupo grupo = grupoRepository.findById(membresia.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + membresia.id_grupo()));

        MembresiaGrupo membresiaEntity = new MembresiaGrupo();
        membresiaEntity.setPerfil(perfil);
        membresiaEntity.setGrupo(grupo);

        MembresiaGrupo saved = membresiaGrupoRepository.save(membresiaEntity);

        LocalDate fechaUnionLocalDate = (saved.getFechaUnion() == null) ? null : saved.getFechaUnion().toLocalDate();

        return new MembresiaGrupoResponseDTO(
                saved.getIdMembresia(),
                saved.getPerfil().getPersona().getNombres(),
                saved.getGrupo().getNombre(),
                fechaUnionLocalDate
        );
    }

    public void delete(Long id) {
        if (!membresiaGrupoRepository.existsById(id)) {
            throw new ResourceNotFoundException("No existe la membresía con el id: " + id);
        }
        membresiaGrupoRepository.deleteById(id);
    }

    public void validarUsuarioEsMiembro(Long idUsuario, Long idGrupo) {
        if (idGrupo == null) {
            throw new BusinessRuleException("El reto no tiene grupo asociado");
        }
        boolean miembro = membresiaGrupoRepository.existsByPerfil_IdAndGrupo_Id(idUsuario, idGrupo);
        if (!miembro) {
            throw new BusinessRuleException("El usuario debe ingresar al grupo antes de participar en el reto");
        }
    }
}