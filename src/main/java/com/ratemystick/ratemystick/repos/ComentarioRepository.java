package com.ratemystick.ratemystick.repos;

import com.ratemystick.ratemystick.domain.Comentario;
import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    Comentario findFirstByUsuario(Usuario usuario);

    Comentario findFirstByPost(Post post);


    List<Comentario> findByPost(Post post);
}
