package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Comentario;
import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.UsuarioDTO;
import com.ratemystick.ratemystick.repos.ComentarioRepository;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.util.NotFoundException;
import com.ratemystick.ratemystick.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PostRepository postRepository;
    private final ComentarioRepository comentarioRepository;

    public UsuarioService(final UsuarioRepository usuarioRepository,
            final PostRepository postRepository, final ComentarioRepository comentarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.postRepository = postRepository;
        this.comentarioRepository = comentarioRepository;
    }

    public List<UsuarioDTO> findAll() {
        final List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> mapToDTO(usuario, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UsuarioDTO usuarioDTO) {
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);
        return usuarioRepository.save(usuario).getId();
    }

    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(usuarioDTO, usuario);
        usuarioRepository.save(usuario);
    }

    public void delete(final Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setContrasena(usuario.getContrasena());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO usuarioDTO, final Usuario usuario) {
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(usuarioDTO.getContrasena());
        return usuario;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Post usuarioPost = postRepository.findFirstByUsuario(usuario);
        if (usuarioPost != null) {
            referencedWarning.setKey("usuario.post.usuario.referenced");
            referencedWarning.addParam(usuarioPost.getId());
            return referencedWarning;
        }
        final Comentario usuarioComentario = comentarioRepository.findFirstByUsuario(usuario);
        if (usuarioComentario != null) {
            referencedWarning.setKey("usuario.comentario.usuario.referenced");
            referencedWarning.addParam(usuarioComentario.getId());
            return referencedWarning;
        }
        return null;
    }

}
