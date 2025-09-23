package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentViewController {

    @Value("${stripe.key.public}")
    private String stripePublicKey;
    @Autowired
    private UserService userService;

    @GetMapping("/pago")
    public String pago(@RequestParam("total") int totalFinal, Model model) {
        // Clave pública de Stripe (para el frontend con Elements)
        model.addAttribute("stripePublicKey", stripePublicKey);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }
        // Total de la compra en pesos colombianos
        model.addAttribute("totalFinal", totalFinal);

        return "Pago";
    }
}
