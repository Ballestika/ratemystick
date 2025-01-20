package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.domain.Usuario;
import com.ratemystick.ratemystick.model.ComentarioDTO;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.repos.UsuarioRepository;
import com.ratemystick.ratemystick.service.ComentarioService;
import com.ratemystick.ratemystick.util.CustomCollectors;
import com.ratemystick.ratemystick.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final UsuarioRepository usuarioRepository;
    private final PostRepository postRepository;

    public ComentarioController(final ComentarioService comentarioService,
            final UsuarioRepository usuarioRepository, final PostRepository postRepository) {
        this.comentarioService = comentarioService;
        this.usuarioRepository = usuarioRepository;
        this.postRepository = postRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("usuarioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getNombre)));
        model.addAttribute("postValues", postRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Post::getId, Post::getImagen)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("comentarios", comentarioService.findAll());
        return "comentario/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("comentario") final ComentarioDTO comentarioDTO) {
        return "comentario/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("comentario") @Valid final ComentarioDTO comentarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comentario/add";
        }
        comentarioService.create(comentarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comentario.create.success"));
        return "redirect:/comentarios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("comentario", comentarioService.get(id));
        return "comentario/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("comentario") @Valid final ComentarioDTO comentarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comentario/edit";
        }
        comentarioService.update(id, comentarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comentario.update.success"));
        return "redirect:/comentarios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        comentarioService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("comentario.delete.success"));
        return "redirect:/comentarios";
    }

}
