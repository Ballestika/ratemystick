package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.model.PostDTO;
import com.ratemystick.ratemystick.model.RatingDTO;
import com.ratemystick.ratemystick.service.ComentarioService;
import com.ratemystick.ratemystick.service.PostService;
import com.ratemystick.ratemystick.service.RatingService;
import org.springframework.stereotype.Controller;
import com.ratemystick.ratemystick.model.ComentarioDTO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    private final PostService postService;
    private final ComentarioService comentarioService;
    private final RatingService ratingService;

    // Inyección del servicio de publicaciones
    public HomeController(PostService postService, ComentarioService comentarioService, RatingService ratingService) {
        this.comentarioService = comentarioService;
        this.postService = postService;
        this.ratingService = ratingService;
    }

    @GetMapping("/")
    public String valorarPost(Model model, RedirectAttributes redirectAttributes) {
        try {
            // Obtener un post aleatorio
            PostDTO postAleatorio = postService.getRandomPost();
            model.addAttribute("post", postAleatorio); // Pasar el post al modelo
            model.addAttribute("rating", new RatingDTO()); // Añadir un objeto RatingDTO vacío
            return "home/index"; // Vista específica para valorar
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No hay posts disponibles para valorar.");
            return "redirect:/posts"; // Redirigir a la lista si ocurre un error
        }
    }


    @PostMapping("/")
    public String registrarValoracion(@RequestParam("postId") Long postId,
                                      @RequestParam("rating") int rating,
                                      final RedirectAttributes redirectAttributes) {
        try {
            // Asegurarse de que se haya seleccionado una valoración
            if (rating < 1 || rating > 5) {
                redirectAttributes.addFlashAttribute("error", "La valoración debe estar entre 1 y 5 estrellas.");
                return "redirect:/home/index"; // Volver a la pantalla de valoración
            }

            // Crear RatingDTO
            RatingDTO ratingDTO = new RatingDTO();
            ratingDTO.setPuntuacion(String.valueOf(rating));
            ratingDTO.setPost(postId);

            // Guardar la valoración
            ratingService.create(ratingDTO);

            redirectAttributes.addFlashAttribute("success", "¡Valoración registrada exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al registrar tu valoración.");
        }
        return "redirect:/home/index"; // Redirigir para valorar otro post
    }



}
