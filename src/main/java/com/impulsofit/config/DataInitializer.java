package com.impulsofit.config;

import com.impulsofit.model.*;
import com.impulsofit.repository.PersonaRepository;
import com.impulsofit.repository.RoleRepository;
import com.impulsofit.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with default data...");

        // Crear roles
        Role userRole = createRoleIfNotExists(RoleType.ROLE_USER);
        Role adminRole = createRoleIfNotExists(RoleType.ROLE_ADMIN);

        // Crear usuario admin
        createAdminUserIfNotExists(adminRole);

        log.info("Database initialization completed.");
    }

    private Role createRoleIfNotExists(RoleType roleType) {
        return roleRepository.findByNombre(roleType)
                .orElseGet(() -> {
                    Role role = new Role(roleType);
                    roleRepository.save(role);
                    log.info("Created role: {}", roleType);
                    return role;
                });
    }

    private void createAdminUserIfNotExists(Role adminRole) {
        String adminEmail = "admin@impulsofit.com";

        if (usuarioRepository.existsByEmailIgnoreCase(adminEmail)) {
            log.info("Admin user already exists: {}", adminEmail);
            return;
        }

        Usuario adminUser = new Usuario();
        adminUser.setEmail(adminEmail);
        adminUser.setContrasena(passwordEncoder.encode("admin123"));
        adminUser.setRole(adminRole);
        adminUser.setBloqueado(false);
        adminUser.setCodPregunta(CodPregunta.MASCOT);
        adminUser.setRespuesta("admin");
        Usuario savedAdmin = usuarioRepository.save(adminUser);

        String fecha = "2000-10-10";
        LocalDate fechaInicio = LocalDate.parse(fecha);
        Persona adminPersona = new Persona();
        adminPersona.setUsuario(savedAdmin);
        adminPersona.setNombres("System Administrator");
        adminPersona.setGenero("F");
        adminPersona.setApellidoM("Administrator");
        adminPersona.setApellidoP("Administrator");
        adminPersona.setFechaNacimiento(fechaInicio);
        personaRepository.save(adminPersona);

        log.info("========================================");
        log.info("DEFAULT ADMIN USER CREATED:");
        log.info("Email: {}", adminEmail);
        log.info("Password: admin123");
        log.info("⚠️  CHANGE THIS PASSWORD IN PRODUCTION!");
        log.info("========================================");
    }
}
