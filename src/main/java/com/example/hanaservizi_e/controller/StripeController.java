package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.Items.ItemPedido;
import com.example.hanaservizi_e.dto.PagoDTO;
import com.example.hanaservizi_e.http.PaymentIntentDTO;
import com.example.hanaservizi_e.model.Pago;
import com.example.hanaservizi_e.model.Pedido;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.*;
import com.example.hanaservizi_e.service.PaymentService;
import com.example.hanaservizi_e.service.impl.CustomUserDetailsImpl;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    TallaStockRepository tallaStockRepository;
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
            @AuthenticationPrincipal Object principal,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("status", "error");
            response.put("message", "Usuario no autenticado");
            return ResponseEntity.status(401).body(response);
        }

        try {
            String email;
            if (principal instanceof CustomUserDetailsImpl customUser) {
                email = customUser.getUsername();
            } else {
                response.put("status", "error");
                response.put("message", "Tipo de usuario no soportado");
                return ResponseEntity.status(400).body(response);
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

            PaymentIntent paymentIntent = paymentService.retrievePaymentIntent(id);

            boolean pagoExistente = pagoRepository.findByStripePaymentId(paymentIntent.getId()).isPresent();

            if (!pagoExistente) {
                if (!"succeeded".equals(paymentIntent.getStatus())) {
                    paymentIntent = paymentService.confirm(id, user.getId());
                }

                // Guardar pago
                Pago pago = new Pago();
                pago.setUsuario(user);
                pago.setTotal(paymentIntent.getAmount() / 100.0);
                pago.setMetodo("Tarjeta");
                pago.setEstado(paymentIntent.getStatus());
                pago.setStripePaymentId(paymentIntent.getId());
                pago.setFecha(LocalDateTime.now());
                pago.setTelefono(pagoDTO.getTelefono());
                pago.setDireccion(pagoDTO.getDireccion());
                pagoRepository.save(pago);

                // Crear pedido asociado
                Pedido pedido = new Pedido();
                pedido.setCliente(user);
                pedido.setFecha(LocalDateTime.now());
                pedido.setEstado("Pendiente");
                pedido.setTotal(pago.getTotal());
                pedido.setPago(pago); // 🔹 vincular el pago al pedido

                // Recuperar productos del carrito de la sesión
                Map<String, Map<String, Object>> carrito = (Map<String, Map<String, Object>>) session.getAttribute("carrito");
                if (carrito != null) {
                    List<ItemPedido> items = new ArrayList<>();
                    for (Map<String, Object> itemMap : carrito.values()) {
                        ItemPedido item = new ItemPedido();
                        item.setNombreProducto((String) itemMap.get("nombre"));
                        item.setTalla((String) itemMap.get("talla"));
                        item.setCantidad((Integer) itemMap.get("cantidad"));
                        item.setPrecio((Double) itemMap.get("precio"));
                        item.setPedido(pedido);
                        items.add(item);

                        // Descontar stock del producto
                        Integer productoId = Integer.valueOf(itemMap.get("id").toString());
                        Producto producto = productoRepository.findById(productoId)
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                        // Si es Moda, se descuenta de la talla específica
                        if ("Moda".equalsIgnoreCase(producto.getCategorias().getNombreCategoria()) && item.getTalla() != null) {
                            tallaStockRepository.findByProductoAndTalla(producto, item.getTalla())
                                    .ifPresentOrElse(tallaStock -> {
                                        int nuevoStock = tallaStock.getStock() - item.getCantidad();
                                        if (nuevoStock < 0) {
                                            throw new RuntimeException("Stock insuficiente para " + producto.getNombre() + " talla " + item.getTalla());
                                        }
                                        tallaStock.setStock(nuevoStock);
                                        tallaStockRepository.save(tallaStock);
                                    }, () -> {
                                        throw new RuntimeException("No existe stock configurado para talla " + item.getTalla());
                                    });

                        } else {
                            // Productos normales -> stock general
                            int nuevoStock = producto.getStock() - item.getCantidad();
                            if (nuevoStock < 0) {
                                throw new RuntimeException("Stock insuficiente para " + producto.getNombre());
                            }
                            producto.setStock(nuevoStock);
                            productoRepository.save(producto);
                        }
                    }
                    pedido.setItems(items);
                }

                pedidoRepository.save(pedido);

                // Vaciar carrito de la sesión
                session.removeAttribute("carrito");

                response.put("status", "success");
                response.put("id", pago.getId());
            } else {
                response.put("status", "already_confirmed");
                response.put("message", "Este pago ya fue registrado anteriormente");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
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

        Pedido pedido = pedidoRepository.findByPago(pago)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        model.addAttribute("pago", pago);
        model.addAttribute("items", pedido.getItems());

        return "confirmacion";
    }


}
