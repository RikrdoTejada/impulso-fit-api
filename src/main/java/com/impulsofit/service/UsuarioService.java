    package com.impulsofit.service;

    import java.time.LocalDate;
    import com.impulsofit.dto.request.UsuarioDTO;
    import com.impulsofit.model.Usuario;
    import com.impulsofit.repository.UsuarioRepository;
    import org.springframework.stereotype.Service;

    @Service
    public class UsuarioService {

        private final UsuarioRepository usuarioRepository;

        public UsuarioService(UsuarioRepository usuarioRepository) {
            this.usuarioRepository = usuarioRepository;
        }

        public Usuario registrarUsuario(UsuarioDTO dto) {
            // Guardar contraseña en plano (NO recomendado en producción)
            Usuario usuario = new Usuario(
                    dto.getNombre(),
                    dto.getEmail(),
                    dto.getContrasena(),
                    dto.getEdad(),
                    dto.getGenero(),
                    LocalDate.now()
            );

            return usuarioRepository.save(usuario);
        }
    }