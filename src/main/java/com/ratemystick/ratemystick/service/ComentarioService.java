package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Comentario;
import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.ComentarioDTO;
import com.ratemystick.ratemystick.repos.ComentarioRepository;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PostRepository postRepository;

    public ComentarioService(final ComentarioRepository comentarioRepository,
            final UsuarioRepository usuarioRepository, final PostRepository postRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.postRepository = postRepository;
    }

    public List<ComentarioDTO> findAll() {
        final List<Comentario> comentarios = comentarioRepository.findAll(Sort.by("id"));
        return comentarios.stream()
                .map(comentario -> mapToDTO(comentario, new ComentarioDTO()))
                .toList();
    }

    public ComentarioDTO get(final Long id) {
        return comentarioRepository.findById(id)
                .map(comentario -> mapToDTO(comentario, new ComentarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ComentarioDTO comentarioDTO) {
        final Comentario comentario = new Comentario();
        mapToEntity(comentarioDTO, comentario);
        return comentarioRepository.save(comentario).getId();
    }

    public void update(final Long id, final ComentarioDTO comentarioDTO) {
        final Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(comentarioDTO, comentario);
        comentarioRepository.save(comentario);
    }

    public void delete(final Long id) {
        comentarioRepository.deleteById(id);
    }

    private ComentarioDTO mapToDTO(final Comentario comentario, final ComentarioDTO comentarioDTO) {
        comentarioDTO.setId(comentario.getId());
        comentarioDTO.setContenido(comentario.getContenido());
        comentarioDTO.setUsuario(comentario.getUsuario() == null ? null : comentario.getUsuario().getId());
        comentarioDTO.setPost(comentario.getPost() == null ? null : comentario.getPost().getId());
        return comentarioDTO;
    }

    private Comentario mapToEntity(final ComentarioDTO comentarioDTO, final Comentario comentario) {
        comentario.setContenido(comentarioDTO.getContenido());
        final Usuario usuario = comentarioDTO.getUsuario() == null ? null : usuarioRepository.findById(comentarioDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        comentario.setUsuario(usuario);
        final Post post = comentarioDTO.getPost() == null ? null : postRepository.findById(comentarioDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        comentario.setPost(post);
        return comentario;
    }

}
