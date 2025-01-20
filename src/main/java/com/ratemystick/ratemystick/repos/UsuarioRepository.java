package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
