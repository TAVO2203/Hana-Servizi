package com.example.hanaservizi_e.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {

        if (principal != null) {
            String nombre = principal.getAttribute("name");
            String foto = principal.getAttribute("picture");

            model.addAttribute("nombreUsuario", nombre);
            model.addAttribute("fotoUsuario", foto);
        }

        return "index";

    }
}

