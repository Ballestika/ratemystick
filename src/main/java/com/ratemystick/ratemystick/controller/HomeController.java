package com.ratemystick.ratemystick.controller;

import com.ratemystick.ratemystick.model.PostDTO;
import com.ratemystick.ratemystick.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final PostService postService;

    // Inyección del servicio de publicaciones
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // Cambia getAllPosts() por findAll()
        List<PostDTO> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "home/index";
    }

}
