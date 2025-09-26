package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.PagoDTO;
import com.example.hanaservizi_e.http.PaymentIntentDTO;
import com.example.hanaservizi_e.model.Pago;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.PagoRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import com.example.hanaservizi_e.service.PaymentService;
import com.example.hanaservizi_e.service.impl.CustomUserDetailsImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/stripe")
@CrossOrigin(origins = "*")
public class StripeController {

    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PagoRepository pagoRepository;

    // Crear PaymentIntent para tarjeta
    @PostMapping("/paymentintent")
    public ResponseEntity<Map<String, String>> payment(@RequestBody PaymentIntentDTO paymentIntentDto) throws StripeException {
        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
        Map<String, String> response = new HashMap<>();
        response.put("client_secret", paymentIntent.getClientSecret());

        System.out.println("Client secret generado: " + paymentIntent.getClientSecret());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> confirm(
            @PathVariable String id,
            @Valid @RequestBody PagoDTO pagoDTO,
            BindingResult result,
            @AuthenticationPrincipal Object principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("status", "error");
            response.put("message", "Usuario no autenticado");
            return ResponseEntity.status(401).body(response);
        }

        if (result.hasErrors()) {
            response.put("status", "error");
            response.put("message", result.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            String email;
            if (principal instanceof OAuth2User oauthUser) {
                email = oauthUser.getAttribute("email");
            } else if (principal instanceof CustomUserDetailsImpl customUser) {
                email = customUser.getUsername();
            } else {
                response.put("status", "error");
                response.put("message", "Tipo de usuario no soportado");
                return ResponseEntity.status(400).body(response);
            }

            // Buscar el usuario en la BD
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

            // Recuperar PaymentIntent desde Stripe
            PaymentIntent paymentIntent = paymentService.retrievePaymentIntent(id);

            // Verificar si ya existe un pago en la BD para este PaymentIntent
            boolean pagoExistente = pagoRepository.findByStripePaymentId(paymentIntent.getId()).isPresent();

            if (!pagoExistente) {
                // Confirmar PaymentIntent solo si no ha sido pagado
                if (!"succeeded".equals(paymentIntent.getStatus())) {
                    paymentIntent = paymentService.confirm(id, user.getId());
                }

                // Guardar pago en BD
                Pago pago = new Pago();
                pago.setUsuario(user);
                pago.setTotal(paymentIntent.getAmount() / 100.0); // convertir a pesos
                pago.setMetodo("Tarjeta");
                pago.setEstado(paymentIntent.getStatus());
                pago.setStripePaymentId(paymentIntent.getId());
                pago.setFecha(LocalDateTime.now());

                pago.setTelefono(pagoDTO.getTelefono());   // form
                pago.setDireccion(pagoDTO.getDireccion());
                pagoRepository.save(pago);

                response.put("status", "success");
                response.put("id", pago.getId());
            } else {
                response.put("status", "already_confirmed");
                response.put("message", "Este pago ya fue registrado anteriormente");
            }

            return ResponseEntity.ok(response);

        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Cancelar pago
    @PostMapping("/cancel/{id}")
    @ResponseBody
    public ResponseEntity<String> cancel(@PathVariable String id) throws StripeException {
        PaymentIntent paymentIntent = paymentService.cancel(id);
        return ResponseEntity.ok(paymentIntent.toJson());
    }

    // Pago Nequi
    @PostMapping("/nequi")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> pagarConNequi(
            @RequestParam String telefono,
            @RequestParam Double total,
            @AuthenticationPrincipal Object principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("status", "error");
            response.put("message", "Usuario no autenticado");
            return ResponseEntity.status(401).body(response);
        }

        // Obtener email del usuario según el tipo
        String email;
        if (principal instanceof OAuth2User oauthUser) {
            email = oauthUser.getAttribute("email");
        } else if (principal instanceof CustomUserDetailsImpl customUser) {
            email = customUser.getUsername();
        } else {
            response.put("status", "error");
            response.put("message", "Tipo de usuario no soportado");
            return ResponseEntity.status(400).body(response);
        }

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            response.put("status", "error");
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }

        Pago pago = paymentService.procesarPagoNequi(telefono, total, user);

        response.put("status", "success");
        response.put("id", pago.getId());

        return ResponseEntity.ok(response);
    }


    // Pago Daviplata
    @PostMapping("/daviplata")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> pagarConDaviplata(@RequestParam String telefono,
                                                                 @RequestParam Double monto,
                                                                 @RequestParam Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        Pago pago = paymentService.procesarPagoDaviplata(telefono, monto, user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("id", pago.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirmacion")
    public String mostrarConfirmacionQuery(Model model, @RequestParam("pagoId") Long pagoId) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        model.addAttribute("pago", pago);
        return "confirmacion";
    }
}
