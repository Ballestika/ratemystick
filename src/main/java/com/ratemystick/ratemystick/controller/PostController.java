package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.PostDTO;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.service.PostService;
import com.ratemystick.ratemystick.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.StandardCopyOption;



@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private static final String UPLOAD_DIR = "src/main/resources/static/images/posts/";

    public PostController(final PostService postService, final UsuarioRepository usuarioRepository) {
        this.postService = postService;
        this.usuarioRepository = usuarioRepository;
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
            // Obtener el usuario autenticado
            System.out.println("Obteniendo el usuario autenticado");
            String email = principal.getName();
            System.out.println("Email del usuario autenticado: " + email);

            Usuario usuario = usuarioRepository.findByCorreo(email)
                    .orElseThrow(() -> {
                        System.out.println("Usuario no encontrado con el email: " + email);
                        return new IllegalStateException("Usuario no encontrado");
                    });

            System.out.println("Usuario encontrado: " + usuario.getNombre());

            // Guardar la imagen en el directorio "uploads"
            System.out.println("Procesando la imagen subida");
            String fileName = file.getOriginalFilename();
            System.out.println("Nombre del archivo: " + fileName);

            if (fileName == null || fileName.isEmpty()) {
                throw new IllegalArgumentException("El archivo de imagen está vacío");
            }

            Path path = Paths.get("uploads/" + fileName);
            System.out.println("Guardando el archivo en la ruta: " + path.toAbsolutePath());

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo guardado correctamente");

            // Crear el PostDTO
            System.out.println("Creando el DTO del post");
            PostDTO postDTO = new PostDTO();
            postDTO.setImagen("/uploads/" + fileName);
            postDTO.setDescripcion(descripcion);
            postDTO.setUsuario(usuario.getId());

            System.out.println("Datos del post: " +
                    " Imagen: " + postDTO.getImagen() +
                    ", Descripción: " + postDTO.getDescripcion() +
                    ", Usuario ID: " + postDTO.getUsuario());

            // Guardar el post
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
