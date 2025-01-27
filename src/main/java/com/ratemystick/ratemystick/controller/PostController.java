package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.ComentarioDTO;
import com.ratemystick.ratemystick.model.PostDTO;
import com.ratemystick.ratemystick.model.RatingDTO;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.service.ComentarioService;
import com.ratemystick.ratemystick.service.PostService;
import com.ratemystick.ratemystick.service.RatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UsuarioRepository usuarioRepository;
    private final RatingService ratingService;
    private final ComentarioService comentarioService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private static final String UPLOAD_DIR = "src/main/resources/static/images/posts/";

    public PostController(final PostService postService, final UsuarioRepository usuarioRepository, RatingService ratingService, ComentarioService comentarioService) {
        this.postService = postService;
        this.usuarioRepository = usuarioRepository;
        this.ratingService = ratingService;
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("posts", postService.findAll());
        return "post/list";
    }

    @GetMapping("/add")
    public String addForm(final Model model) {
        model.addAttribute("post", new PostDTO());
        return "post/add";
    }

    @PostMapping("/add")
    public String add(@RequestParam("imagen") MultipartFile file,
                      @RequestParam("descripcion") String descripcion,
                      Principal principal,
                      final RedirectAttributes redirectAttributes) {
        System.out.println("Entrando al método de agregar post");

        try {
            // Verifica si el archivo es válido
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("El archivo de imagen no se ha proporcionado o está vacío");
            }

            // Obtener el usuario autenticado o asignar el usuario con ID 1 si no hay usuario autenticado
            System.out.println("Obteniendo el usuario autenticado");
            Usuario usuario;
            if (principal != null) {
                String email = principal.getName();
                usuario = usuarioRepository.findByCorreo(email)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con el email: " + email));
                System.out.println("Usuario autenticado: " + usuario.getNombre());
            } else {
                // Si no hay usuario autenticado, se asigna el usuario con id 1
                usuario = usuarioRepository.findById(1L)
                        .orElseThrow(() -> new IllegalStateException("Usuario con id 1 no encontrado"));
                System.out.println("Usuario asignado por defecto: " + usuario.getNombre());
            }

            // Ruta para guardar la imagen en "resources/static/uploads"
            String uploadsDir = "src/main/resources/static/uploads";
            Path uploadPath = Paths.get(uploadsDir);

            // Crea el directorio si no existe
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Directorio creado en: " + uploadPath.toAbsolutePath());
            }

            // Guardar la imagen
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Imagen guardada en: " + filePath.toAbsolutePath());

            // Crear el PostDTO
            PostDTO postDTO = new PostDTO();
            postDTO.setImagen("/uploads/" + fileName); // Ruta accesible desde el navegador
            postDTO.setDescripcion(descripcion);
            postDTO.setUsuario(usuario.getId());
            postService.create(postDTO);
            System.out.println("Post creado correctamente");

            redirectAttributes.addFlashAttribute("success", "Post creado correctamente");
            return "redirect:/posts";
        } catch (Exception e) {
            System.err.println("Error al subir el post: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al subir el post: " + e.getMessage());
            return "redirect:/posts/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("post", postService.get(id));
        return "post/edit";
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PostDTO post = postService.get(id);

            // Calcular el rating promedio
            double puntos = postService.getTotalPuntos(id);
            long cantidadVotos = postService.getCantidadVotos(id);
            double promedio = cantidadVotos > 0 ? (double) puntos / cantidadVotos : 0;
            post.setRating(Math.round(promedio * 10) / 10.0);

            // Obtener comentarios para el post
            List<ComentarioDTO> comentarios = comentarioService.findByPostId(id);
            post.setComentarios(comentarios);  // Establecer los comentarios en el post

            model.addAttribute("post", post);
            return "post/post"; // Devuelve la vista con los comentarios
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "El post no fue encontrado.");
            return "redirect:/posts";
        }
    }




    @GetMapping("/listar")
    public String index(Model model) {
        try {
            // Llama a findAll() para obtener todos los posts
            List<PostDTO> posts = postService.findAll();

            // Para cada post, calcula el rating promedio y asigna comentarios
            for (PostDTO post : posts) {
                double puntos = postService.getTotalPuntos(post.getId());
                long cantidadVotos = postService.getCantidadVotos(post.getId());
                double promedio = cantidadVotos > 0 ? (double) puntos / cantidadVotos : 0;
                post.setRating(Math.round(promedio * 10) / 10.0);

                // Obtiene los comentarios del post
                List<ComentarioDTO> comentarios = comentarioService.findByPostId(post.getId());
                post.setComentarios(comentarios);
            }

            model.addAttribute("posts", posts);
            return "post/listar"; // Devuelve la vista principal con la lista de posts
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo cargar la lista de posts.");
            return "post/listar"; // Puedes redirigir o mostrar un mensaje de error en
        }
    }


    @PostMapping("/comentario")
    public String registrarComentario(@RequestParam("contenido") String contenido,
                                      @RequestParam("postId") Long postId,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario autenticado (si hay uno)
            Usuario usuario;
            if (principal != null) {
                String email = principal.getName();
                usuario = usuarioRepository.findByCorreo(email)
                        .orElseThrow(() -> new IllegalStateException("Usuario no encontrado con el email: " + email));
            } else {
                // Si no hay usuario autenticado, asignamos el usuario con id = 1
                usuario = usuarioRepository.findById(1L)
                        .orElseThrow(() -> new IllegalStateException("Usuario con id 1 no encontrado"));
            }

            // Crear el ComentarioDTO y asociarlo al post y al usuario
            ComentarioDTO comentarioDTO = new ComentarioDTO();
            comentarioDTO.setContenido(contenido);
            comentarioDTO.setPost(postId);
            comentarioDTO.setUsuario(usuario.getId()); // Asignar el usuario correctamente

            // Registrar el comentario
            comentarioService.create(comentarioDTO);

            // Agregar mensaje de éxito
            redirectAttributes.addFlashAttribute("success", "Comentario agregado correctamente.");
            return "redirect:/posts/" + postId; // Redirigir al post donde se agregó el comentario
        } catch (Exception e) {
            // Manejo de error si ocurre
            redirectAttributes.addFlashAttribute("error", "Error al agregar el comentario.");
            return "redirect:/posts/" + postId;
        }
    }

    /*
    @PostMapping("/edit/{id}")
    public String editPost(
            @PathVariable(name = "id") final Long id,
            @RequestParam("imagen") MultipartFile imagen,
            @ModelAttribute("post") @Valid final PostDTO postDTO,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "post/edit";
        }

        // Obtener usuario autenticado
        String username = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        // Guardar nueva imagen si fue proporcionada
        if (!imagen.isEmpty()) {
            String imagePath = guardarImagenEnServidor(imagen);
            postDTO.setImagen(imagePath);
        }
        postDTO.setUsuario(usuario.getId());

        // Actualizar el post
        postService.update(id, postDTO);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.update.success"));
        return "redirect:/posts";
    }
*/
    private String guardarImagenEnServidor(MultipartFile imagen) {
        if (imagen.isEmpty()) {
            throw new RuntimeException("El archivo está vacío");
        }
        try {
            String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);
            Files.createDirectories(rutaArchivo.getParent()); // Crear directorio si no existe
            Files.copy(imagen.getInputStream(), rutaArchivo);
            return "/images/posts/" + nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

}
