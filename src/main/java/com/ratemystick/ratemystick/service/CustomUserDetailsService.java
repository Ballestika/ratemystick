package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with correo: " + username));

        String[] roles = usuario.getRoles().split(","); // Maneja múltiples roles si es necesario
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .roles(roles) // Asigna roles al usuario
                .build();
    }
}

