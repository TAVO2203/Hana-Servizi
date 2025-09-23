package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.http.PaymentIntentDTO;
import com.example.hanaservizi_e.model.Pago;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.PagoRepository;
import com.example.hanaservizi_e.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    private String secretKey;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Crear un PaymentIntent en Stripe
     */
    public PaymentIntent paymentIntent(PaymentIntentDTO paymentIntentDto) throws StripeException {
        Stripe.apiKey = secretKey;

        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount()); // en centavos
        params.put("currency", paymentIntentDto.getCurrency().toString());
        params.put("description", paymentIntentDto.getDescription());
        params.put("payment_method_types", paymentMethodTypes);

        System.out.println("Creando PaymentIntent -> amount=" + paymentIntentDto.getAmount() +
                " currency=" + paymentIntentDto.getCurrency() +
                " description=" + paymentIntentDto.getDescription());


        return PaymentIntent.create(params);
    }

    /**
     * Confirmar un PaymentIntent y registrar el pago en la BD
     */
    public PaymentIntent confirm(String id, Long userId) throws StripeException {
        Stripe.apiKey = secretKey;

        // 1. Recuperar PaymentIntent de Stripe
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);

        // 2. Confirmar el PaymentIntent
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa"); // ⚠️ En producción debe venir del frontend
        paymentIntent = paymentIntent.confirm(params);

        // 3. Guardar en BD
        Pago pago = new Pago();
        pago.setStripePaymentId(paymentIntent.getId());
        pago.setTotal(paymentIntent.getAmount() / 100.0); // Stripe maneja centavos
        pago.setEstado(paymentIntent.getStatus());
        pago.setFecha(LocalDateTime.now());

        // Relacionar con usuario
        User usuario = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        pago.setUsuario(usuario);

        pagoRepository.save(pago);

        return paymentIntent;
    }

    /**
     * Cancelar un PaymentIntent
     */
    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretKey;

        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();

        return paymentIntent;
    }

    public Pago procesarPagoNequi(String telefono, Double total, User user) {
        Pago pago = new Pago();
        pago.setMetodo("nequi");
        pago.setTelefono(telefono);
        pago.setTotal(total);
        pago.setFecha(LocalDateTime.now());
        pago.setEstado("success");
        pago.setUsuario(user);

        return pagoRepository.save(pago); // retorna el objeto guardado
    }


    public Pago procesarPagoDaviplata(String telefono, Double monto, User user) {
        Pago pago = new Pago();
        pago.setMetodo("daviplata");
        pago.setTelefono(telefono);
        pago.setTotal(monto);
        pago.setFecha(LocalDateTime.now());
        pago.setEstado("success");
        pago.setUsuario(user);

        return pagoRepository.save(pago);
    }

    public PaymentIntent retrievePaymentIntent(String paymentIntentId) throws StripeException {
        return PaymentIntent.retrieve(paymentIntentId);
    }

}

