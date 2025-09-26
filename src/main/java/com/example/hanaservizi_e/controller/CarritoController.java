package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UserService userService;

    @GetMapping
    public String mostrarCarrito(HttpSession session, Model model) {
        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);

        double subtotal = 0;
        for (Map<String, Object> item : carrito.values()) {
            double precio = (double) item.get("precio");
            int cantidad = (int) item.get("cantidad");
            subtotal += precio * cantidad;
        }

        double descuentoAplicado = 0;
        double totalFinal = subtotal - descuentoAplicado;

        model.addAttribute("carrito", carrito.entrySet());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuentoAplicado", descuentoAplicado);
        model.addAttribute("totalFinal", totalFinal);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }

        return "carrito";

    }

    @GetMapping("/agregar")
    public String agregarAlCarrito(@RequestParam String id,
                                   @RequestParam String nombre,
                                   @RequestParam double precio,
                                   @RequestParam String imagen,
                                   @RequestParam int cantidad,
                                   @RequestParam(required = false) String talla,
                                   HttpSession session) {

        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);

        // La clave será id + talla para diferenciar productos del mismo id pero distinta talla
        String key = talla != null ? id + "-" + talla : id;

        if (carrito.containsKey(key)) {
            int cantidadExistente = (int) carrito.get(key).get("cantidad");
            carrito.get(key).put("cantidad", cantidadExistente + cantidad);
        } else {
            Map<String, Object> producto = new HashMap<>();
            producto.put("id", id);
            producto.put("nombre", nombre);
            producto.put("precio", precio);
            producto.put("imagen", imagen);
            producto.put("cantidad", cantidad);
            producto.put("talla", talla); 
            carrito.put(key, producto);
        }

        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }


    @PostMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable String id, HttpSession session) {
        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);
        carrito.remove(id);
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    private Map<String, Map<String, Object>> obtenerCarrito(HttpSession session) {
        Map<String, Map<String, Object>> carrito =
                (Map<String, Map<String, Object>>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new LinkedHashMap<>();
            session.setAttribute("carrito", carrito);
        }

        return carrito;
    }
    @PostMapping("/incrementar/{id}")
    public String incrementarCantidad(@PathVariable String id, HttpSession session) {
        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);
        if (carrito.containsKey(id)) {
            int cantidadActual = (int) carrito.get(id).get("cantidad");
            carrito.get(id).put("cantidad", cantidadActual + 1);
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }
    @PostMapping("/actualizar/{id}")
    public String actualizarCantidad(@PathVariable String id,
                                     @RequestParam int cantidad,
                                     HttpSession session) {
        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);

        if (carrito.containsKey(id)) {
            if (cantidad > 0) {
                carrito.get(id).put("cantidad", cantidad);
            } else {
                carrito.remove(id); // si el input es 0, elimina
            }
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }


    @PostMapping("/decrementar/{id}")
    public String decrementarCantidad(@PathVariable String id, HttpSession session) {
        Map<String, Map<String, Object>> carrito = obtenerCarrito(session);
        if (carrito.containsKey(id)) {
            int cantidadActual = (int) carrito.get(id).get("cantidad");
            if (cantidadActual > 1) {
                carrito.get(id).put("cantidad", cantidadActual - 1);
            } else {

                carrito.remove(id);
            }
        }
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }
}
