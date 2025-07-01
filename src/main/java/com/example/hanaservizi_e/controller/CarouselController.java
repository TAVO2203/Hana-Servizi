package com.example.hanaservizi_e.controller;


import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CarouselController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductoRepository productoRepository;
    @GetMapping("/carousel")
    public String mostrarCarrusel() {
        return "carousel";
    }

    @GetMapping("/filtros")
    public String listarProductos(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category,
            Model model) {

        List<Producto> productos;

        if (search != null && !search.isEmpty()) {
            productos = productoRepository.findByNombreContainingIgnoreCase(search);
        } else if (category != null && !category.isEmpty()) {
            productos = productoRepository.findByCategorias_NombreCategoriaIgnoreCase(category);
        } else {
            productos = productoRepository.findAll();
        }

        for (Producto producto : productos) {
            if (producto.getImagen() != null && !producto.getImagen().startsWith("/img/")) {
                producto.setImagen("/img/" + producto.getImagen());
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String email = auth.getName();
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }

        model.addAttribute("productos", productos);
        return "Filtros";
    }
}
