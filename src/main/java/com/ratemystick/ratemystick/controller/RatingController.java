package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.domain.Post;
import com.ratemystick.ratemystick.model.RatingDTO;
import com.ratemystick.ratemystick.repos.PostRepository;
import com.ratemystick.ratemystick.service.RatingService;
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
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final PostRepository postRepository;

    public RatingController(final RatingService ratingService,
            final PostRepository postRepository) {
        this.ratingService = ratingService;
        this.postRepository = postRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("postValues", postRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Post::getId, Post::getImagen)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("ratings", ratingService.findAll());
        return "rating/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("rating") final RatingDTO ratingDTO) {
        return "rating/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("rating") @Valid final RatingDTO ratingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rating/add";
        }
        ratingService.create(ratingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rating.create.success"));
        return "redirect:/ratings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("rating", ratingService.get(id));
        return "rating/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("rating") @Valid final RatingDTO ratingDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "rating/edit";
        }
        ratingService.update(id, ratingDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("rating.update.success"));
        return "redirect:/ratings";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        ratingService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("rating.delete.success"));
        return "redirect:/ratings";
    }

}
