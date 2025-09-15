package com.example.hanaservizi_e.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collection;

@Controller
public class RedireccionarController {

    @GetMapping("/redireccionar-por-rol")
    public String redireccionarPorRol(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            // Log temporal para debug
            System.out.println("Authorities en redirección: " + authorities);

            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_VENDEDOR"))) {
                return "redirect:/vendedor/dashboard";
            } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENTE"))) {
                return "redirect:/cliente/dashboard";
            }
        }
        return "redirect:/cliente/dashboard";
    }
}