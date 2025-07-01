package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.LoginDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }
}
