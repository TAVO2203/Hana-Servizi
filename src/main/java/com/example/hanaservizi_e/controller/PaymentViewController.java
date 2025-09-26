package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.UserRepository;
import com.example.hanaservizi_e.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class PaymentViewController {

    @Value("${stripe.key.public}")
    private String stripePublicKey;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pago")
    public String pago(Model model, Principal principal, HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User userAuth = userService.buscarPorEmail(email).orElse(null);
            if (userAuth != null) {
                model.addAttribute("nombreUsuario", userAuth.getUsername());
                model.addAttribute("rolUsuario", userAuth.getRol().getRolname());
            }
        }

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Recuperar carrito de la sesión
        Map<String, Map<String, Object>> carrito =
                (Map<String, Map<String, Object>>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new LinkedHashMap<>();
        }

        // Calcular subtotal
        double subtotal = 0;
        for (Map<String, Object> item : carrito.values()) {
            double precio = (double) item.get("precio");
            int cantidad = (int) item.get("cantidad");
            subtotal += precio * cantidad;
        }

        double descuentoAplicado = 0;
        double totalFinal = subtotal - descuentoAplicado;

        // Datos de Stripe y usuario
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("telefono", user.getPhone());
        model.addAttribute("direccion", user.getAddress());

        // Pasar carrito y totales
        model.addAttribute("carrito", carrito.values());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuentoAplicado", descuentoAplicado);
        model.addAttribute("totalFinal", totalFinal);

        return "Pago"; // vista pago.html
    }
}
