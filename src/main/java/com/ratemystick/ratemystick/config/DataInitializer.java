package com.ratemystick.ratemystick.config;

import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
/*
@Configuration
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            // Crear usuario con rol USER
            Optional<Usuario> existingUser = usuarioRepository.findByCorreo("user@example.com");
            if (existingUser.isEmpty()) {
                Usuario user = new Usuario();
                user.setNombre("User");
                user.setCorreo("user@example.com");
                user.setContrasena(passwordEncoder.encode("password")); // Contraseña codificada
                user.setRoles("USER"); // Asegúrate de tener este campo en tu entidad
                usuarioRepository.save(user);
                System.out.println("Usuario USER creado.");
            }

            // Crear usuario con rol ADMIN
            Optional<Usuario> existingAdmin = usuarioRepository.findByCorreo("admin@example.com");
            if (existingAdmin.isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setCorreo("admin@example.com");
                admin.setContrasena(passwordEncoder.encode("adminpassword")); // Contraseña codificada
                admin.setRoles("ADMIN"); // Asegúrate de tener este campo en tu entidad
                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado.");
            }
        };
    }
}*/
