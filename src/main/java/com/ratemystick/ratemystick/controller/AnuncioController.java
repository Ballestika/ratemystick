package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.model.AnuncioDTO;
import com.ratemystick.ratemystick.service.AnuncioService;
import com.ratemystick.ratemystick.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/anuncios")
public class AnuncioController {

    private final AnuncioService anuncioService;

    public AnuncioController(final AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("anuncios", anuncioService.findAll());
        return "anuncio/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("anuncio") final AnuncioDTO anuncioDTO) {
        return "anuncio/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("anuncio") @Valid final AnuncioDTO anuncioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "anuncio/add";
        }
        anuncioService.create(anuncioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("anuncio.create.success"));
        return "redirect:/anuncios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("anuncio", anuncioService.get(id));
        return "anuncio/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("anuncio") @Valid final AnuncioDTO anuncioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "anuncio/edit";
        }
        anuncioService.update(id, anuncioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("anuncio.update.success"));
        return "redirect:/anuncios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        anuncioService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("anuncio.delete.success"));
        return "redirect:/anuncios";
    }

}
