package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.ClienteDto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {
    //Inyeccion dependencias//
    private final UserService userService;
    //Constructor//
    public ClienteController(UserService userService) {
        this.userService = userService;
    }
    //Paths cliente//
    @GetMapping("/dashboard")
    public String mostrarDashboardCliente(Authentication authentication, Model model) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);
        model.addAttribute("usuario", usuario);
        return "cliente/dashboard";
    }
    @GetMapping("/editar-perfil")
    public String mostrarEditarPerfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                ClienteDto dto = new ClienteDto();
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setPhone(user.getPhone());
                dto.setAddress(user.getAddress());
                model.addAttribute("clienteDto", dto);
            }
        }
        return "editarPerfilCliente";
    }



}
