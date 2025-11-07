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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RetoService {

    private static final Logger logger = LoggerFactory.getLogger(RetoService.class);

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UnidadRepository unidadRepository;
    private final RetoRepository retoRepository;
    private final MembresiaGrupoRepository membresiaGrupoRepository;


    @Transactional
    public RetoResponseDTO create(RetoRequestDTO reto){

        // Recuperar referencias y validar pertenencia/compatibilidad
        RequestContext ctx = resolveReferences(reto);

        // Validar payload (título/descr/fechas) y unicidad (create)
        validateRetoPayload(reto, null);

        Reto retoEntity = new Reto();
        applyDtoToEntity(retoEntity, reto, ctx);

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

        // recuperar y validar referencias (usuario, grupo, unidad) y pertenencia
        RequestContext ctx = resolveReferences(reto);

        // Validar payload (título/descr/fechas) y unicidad (update)
        validateRetoPayload(reto, id);

        retoEntity.setIdReto(id);
        applyDtoToEntity(retoEntity, reto, ctx);

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

    // Nuevo: record para devolver contexto común
    private record RequestContext(Usuario usuario, Grupo grupo, Unidad unidad) {}

    // Recupera y valida las referencias comunes a create/update
    private RequestContext resolveReferences(RetoRequestDTO reto) {
        Usuario usuario = usuarioRepository.findById(reto.id_usuario_creador())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el usuario con el id: " + reto.id_usuario_creador()));

        Grupo grupo = grupoRepository.findById(reto.id_grupo())
                .orElseThrow(() -> new ResourceNotFoundException("No existe el grupo con el id: " + reto.id_grupo()));

        Unidad unidad = unidadRepository.findById(reto.id_unidad())
                .orElseThrow(() -> new ResourceNotFoundException("No existe la unidad con el id: " + reto.id_unidad()));

        // Validar compatibilidad unidad <-> deporte del grupo
        validarUnidadPermitidaParaDeporte(grupo.getDeporte(), unidad);

        // Validar que el usuario pertenezca al grupo
        if(!membresiaGrupoRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupo(reto.id_usuario_creador(), reto.id_grupo())) {
            throw new ResourceNotFoundException("El usuario con el id: " + reto.id_usuario_creador() + " no pertenece al grupo con el id: " + reto.id_grupo());
        }

        return new RequestContext(usuario, grupo, unidad);
    }

    // Verifica que la unidad seleccionada sea válida para el deporte del grupo
    private void validarUnidadPermitidaParaDeporte(Deporte deporte, Unidad unidad) {
        if (deporte == null || unidad == null) return; // otras validaciones fallarán en su lugar
        String nombreDeporte = deporte.getNombre() == null ? "" : deporte.getNombre();
        String tipoDeporte = (deporte.getTipo() != null ? deporte.getTipo() : deporte.getTipoDeporte());
        String d = (nombreDeporte + " " + (tipoDeporte == null ? "" : tipoDeporte)).toLowerCase();
        Long idUnidad = unidad.getIdUnidad();

        // log inicial
        logger.info("validarUnidadPermitidaParaDeporte: deporte='{}' tipo='{}' combinado='{}' unidadId={} unidadNombre='{}' unidadUso='{}'",
                nombreDeporte, tipoDeporte, d, idUnidad, unidad.getNombre(), unidad.getUso());

        // Detectar categorías de la unidad por nombre/uso (robusto si los ids no coinciden)
        Set<String> unitCategories = detectUnitCategories(unidad);

        // IDs de unidad permitidos por deporte (fallback)
        Set<Long> allowedIds;
        Set<String> allowedCategories;
        if (equalsOrContains(d, "cardio")) {
            // Cardio: Distancia / Tiempo
            allowedIds = Set.of(3L, 4L, 1L, 2L);
            allowedCategories = Set.of("DISTANCIA", "TIEMPO");
        } else if (equalsOrContains(d, "fuerza")) {
            // Fuerza: Días / Peso
            allowedIds = Set.of(5L, 6L);
            allowedCategories = Set.of("DIAS", "PESO");
        } else if (equalsOrContains(d, "resistencia")) {
            // Resistencia Muscular: Series / Días
            allowedIds = Set.of(7L, 5L);
            allowedCategories = Set.of("SERIES", "DIAS");
        } else if (equalsOrContains(d, "cuerpo") || equalsOrContains(d, "mente") || equalsOrContains(d, "cuerpo y mente")) {
            // Cuerpo y Mente: Sesiones / Minutos
            allowedIds = Set.of(8L, 1L);
            allowedCategories = Set.of("SESIONES", "TIEMPO");
        } else if (equalsOrContains(d, "recreativo")) {
            // Recreativo: Puntos / Sesiones / Tiempo
            allowedIds = Set.of(9L, 8L, 1L, 2L);
            allowedCategories = Set.of("PUNTOS", "SESIONES", "TIEMPO");
        } else {
            allowedIds = null;
            allowedCategories = null;
        }

        boolean ok = false;
        if (allowedIds == null && allowedCategories == null) {
            ok = true;
        } else {
            if (idUnidad != null && allowedIds != null && allowedIds.contains(idUnidad)) {
                ok = true;
            } else if (allowedCategories != null) {
                for (String c : unitCategories) {
                    if (allowedCategories.contains(c)) { ok = true; break; }
                }
            }
        }

        if (!ok) {
            logger.warn("Unidad no permitida: deporte='{}' tipo='{}' -> idUnidad={} unidadCategories={} allowedIds={} allowedCategories={}",
                    nombreDeporte, tipoDeporte, idUnidad, unitCategories, allowedIds, allowedCategories);
            throw new BusinessRuleException("Unidad ingresada incorrecta");
        }
    }

    private Set<String> detectUnitCategories(Unidad unidad) {
        String nombre = unidad.getNombre() == null ? "" : unidad.getNombre().toLowerCase();
        String uso = unidad.getUso() == null ? "" : unidad.getUso().toLowerCase();
        java.util.HashSet<String> cats = new java.util.HashSet<>();
        if (nombre.contains("tiempo") || uso.contains("min") || uso.contains("hora")) cats.add("TIEMPO");
        if (nombre.contains("distancia") || uso.contains("met") || uso.contains("kil")) cats.add("DISTANCIA");
        if (nombre.contains("peso") || uso.contains("kilogr")) cats.add("PESO");
        if (nombre.contains("entren") || uso.contains("dia")) cats.add("DIAS");
        if (nombre.contains("series") || uso.contains("repet")) cats.add("SERIES");
        if (nombre.contains("sesion") || uso.contains("sesiones")) cats.add("SESIONES");
        if (nombre.contains("punto") || uso.contains("puntos")) cats.add("PUNTOS");
        return cats;
    }
    private boolean equalsOrContains(String text, String keyword) {
        if (text == null || keyword == null) return false;
        String t = text.toLowerCase();
        String k = keyword.toLowerCase();
        return t.equals(k) || t.contains(k);
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

    // Valida campos comunes de payload para create/update y la unicidad del título
    private void validateRetoPayload(RetoRequestDTO reto, Long existingRetoId) {
        if (reto.descripcion() == null) {
            throw new BusinessRuleException("La descripción no puede estar vacía.");
        }
        if (reto.titulo() == null || reto.titulo().length() < 5) {
            throw new BusinessRuleException("El título debe tener al menos 5 caracteres. Longitud actual: " + (reto.titulo() == null ? 0 : reto.titulo().length()));
        }
        if (existingRetoId == null) {
            // create: comprobar unicidad simple
            if (retoRepository.existsByTituloIgnoreCaseAndGrupo_IdGrupo(reto.titulo(), reto.id_grupo())) {
                throw new AlreadyExistsException("Ya existe un reto con el título: " + reto.titulo());
            }
        } else {
            // update: comprobar unicidad excluyendo el reto actual
            if (retoRepository.existsByTituloIgnoreCaseAndGrupo_IdGrupoAndIdRetoNot(reto.titulo(), reto.id_grupo(), existingRetoId)) {
                throw new AlreadyExistsException("Ya existe un reto con el título: " + reto.titulo());
            }
        }
        // fechas
        if (reto.fecha_inicio().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de inicio no puede ser anterior a la fecha actual. Fecha ingresada: " + reto.fecha_inicio());
        }
        if (reto.fecha_fin().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("La fecha de fin no puede ser anterior a la fecha actual. Fecha ingresada: " + reto.fecha_fin());
        }
    }

    // Aplica valores comunes del DTO a la entidad Reto usando el contexto resuelto
    private void applyDtoToEntity(Reto entity, RetoRequestDTO dto, RequestContext ctx) {
        entity.setGrupo(ctx.grupo());
        entity.setCreador(ctx.usuario());
        entity.setUnidad(ctx.unidad());
        entity.setDescripcion(dto.descripcion());
        entity.setTitulo(dto.titulo());
        entity.setObjetivoTotal(dto.objetivo_total());
        entity.setFechaInicio(dto.fecha_inicio());
        entity.setFechaFin(dto.fecha_fin());
    }
}
