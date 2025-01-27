package com.ratemystick.ratemystick.service;

import com.ratemystick.ratemystick.domain.Comentario;
import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Rating;
import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.PostDTO;
import com.ratemystick.ratemystick.repos.ComentarioRepository;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.repos.RatingRepository;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.util.NotFoundException;
import com.ratemystick.ratemystick.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;
    private final RatingRepository ratingRepository;
    private final ComentarioRepository comentarioRepository;

    public PostService(final PostRepository postRepository,
                       final UsuarioRepository usuarioRepository,
                       final RatingRepository ratingRepository,
                       final ComentarioRepository comentarioRepository) {
        this.postRepository = postRepository;
        this.usuarioRepository = usuarioRepository;
        this.ratingRepository = ratingRepository;
        this.comentarioRepository = comentarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PostDTO> findAll() {
        final List<Post> posts = postRepository.findAll(Sort.by("id").descending());
        return posts.stream()
                .map(post -> mapToDTO(post, new PostDTO()))
                .toList();
    }


    public PostDTO get(final Long id) {
        return postRepository.findById(id)
                .map(post -> mapToDTO(post, new PostDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    public PostDTO getRandomPost() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new IllegalStateException("No hay posts disponibles.");
        }
        // Elegir un post aleatorio
        int randomIndex = ThreadLocalRandom.current().nextInt(posts.size());
        Post postAleatorio = posts.get(randomIndex);

        // Crear un PostDTO vacío
        PostDTO postDTO = new PostDTO();

        // Llamar a mapToDTO con ambos argumentos
        return mapToDTO(postAleatorio, postDTO);
    }

    public double getTotalPuntos(Long postId) {
        return ratingRepository.findByPostId(postId)
                .stream()
                .mapToDouble(rating -> {
                    try {
                        return Double.parseDouble(rating.getPuntuacion().toString());
                    } catch (NumberFormatException e) {
                        System.err.println("Error al convertir la puntuación a double: " + rating.getPuntuacion());
                        return 0; // Puedes manejarlo como prefieras
                    }
                })
                .sum();
    }

    public long getCantidadVotos(Long postId) {
        return ratingRepository.findByPostId(postId).size();
    }

    public Long create(final PostDTO postDTO) {
        final Post post = new Post();
        mapToEntity(postDTO, post);
        return postRepository.save(post).getId();
    }

    public void update(final Long id, final PostDTO postDTO) {
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postDTO, post);
        postRepository.save(post);
    }

    public void delete(final Long id) {
        postRepository.deleteById(id);
    }

    private PostDTO mapToDTO(final Post post, final PostDTO postDTO) {
        postDTO.setId(post.getId());
        postDTO.setImagen(post.getImagen());
        postDTO.setDescripcion(post.getDescripcion());

        // Mapear el nombre del usuario
        if (post.getUsuario() != null) {
            postDTO.setNombreUsuario(post.getUsuario().getNombre());
        }

        // Contar el número de likes
        if (post.getRatings() != null) {
            postDTO.setRating(post.getRatings().size());
        } else {
            postDTO.setRating(0);
        }

        // Extraer comentarios
        if (post.getComentarios() != null) {
            List<String> comentarios = post.getComentarios().stream()
                    .map(Comentario::getContenido) // Obtener el contenido de cada comentario
                    .toList();
            postDTO.setComentarios(comentarios);
        }

        return postDTO;
    }

    private Post mapToEntity(final PostDTO postDTO, final Post post) {
        post.setImagen(postDTO.getImagen());
        post.setDescripcion(postDTO.getDescripcion());

        final Usuario usuario = postDTO.getUsuario() == null ? null :
                usuarioRepository.findById(postDTO.getUsuario())
                        .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        post.setUsuario(usuario);

        return post;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        final Rating postRating = ratingRepository.findFirstByPost(post);
        if (postRating != null) {
            referencedWarning.setKey("post.rating.post.referenced");
            referencedWarning.addParam(postRating.getId());
            return referencedWarning;
        }

        final Comentario postComentario = comentarioRepository.findFirstByPost(post);
        if (postComentario != null) {
            referencedWarning.setKey("post.comentario.post.referenced");
            referencedWarning.addParam(postComentario.getId());
            return referencedWarning;
        }

        return null;
    }
}
