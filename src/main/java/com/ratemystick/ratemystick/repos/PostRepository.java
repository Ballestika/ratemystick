package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {

    Post findFirstByUsuario(Usuario usuario);

}
