package com.example.hanaservizi_e.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentViewController {

    @Value("${stripe.key.public}")
    private String stripePublicKey;

    @GetMapping("/pago")
    public String pago(@RequestParam("total") int totalFinal, Model model) {
        // Clave pública de Stripe (para el frontend con Elements)
        model.addAttribute("stripePublicKey", stripePublicKey);

        // Total de la compra en pesos colombianos
        model.addAttribute("totalFinal", totalFinal);

        return "pago"; // Renderiza pago.html
    }
}
