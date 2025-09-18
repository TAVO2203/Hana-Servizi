package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.model.Notificacion;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.service.NotificacionService;
import com.example.hanaservizi_e.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final Logger logg = LoggerFactory.getLogger(ProductoController.class);
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/index")
    public String mostrarProductosAleatorios(Model model) {
        List<Producto> todos = productoRepository.findByActivoTrue();
        Collections.shuffle(todos);

        List<Categorias> categorias = categoriaRepository.findAllByOrderByIdAsc();
        List<Producto> productosAleatorios = todos.stream()
                .limit(6)
                .toList();

        model.addAttribute("categorias", categorias);
        model.addAttribute("productos", productosAleatorios);
        agregarDatosUsuario(model);

        return "index";
    }

    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        List<Producto> productos = productoRepository.findByActivoTrue();

        model.addAttribute("productos", productos);

        // productos destacados (los primeros 4 si hay suficientes)
        if (productos.size() > 4) {
            model.addAttribute("destacados", productos.subList(0, 4));
        } else {
            model.addAttribute("destacados", productos);
        }

        agregarDatosUsuario(model);

        return "catalogo";
    }


    @GetMapping("/categoria/{id}")
    public String ProductosPorCategoria(@PathVariable("id") Long id, Model model) {
        List<Producto> productos = productoRepository.findByCategoriasIdAndActivoTrue(id);

        agregarDatosUsuario(model);

        Optional<Categorias> categoriaOpt = categoriaRepository.findById(id);
        String nombreCategoria = categoriaOpt.map(Categorias::getNombreCategoria).orElse("Categoría no encontrada");

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaNombre", nombreCategoria);

        return "FiltrosCategorias";
    }
    @GetMapping("/notificaciones")
    public String verNotificaciones(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User usuario = userService.buscarPorEmail(auth.getName()).orElse(null);

            if (usuario != null) {
                List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuario);
                model.addAttribute("notificaciones", notificaciones);
            }
        }

        agregarDatosUsuario(model);
        return "Notificaciones";
    }



    @PostMapping("/notificaciones/marcar-leida/{id}")
    public String marcarNotificacionLeida(@PathVariable Long id, Authentication authentication, RedirectAttributes ra) {

        notificacionService.marcarComoLeida(id);
        return "redirect:/productos/notificaciones";
    }

    @PostMapping("/notificaciones/eliminar/{id}")
    public String eliminarNotificacion(@PathVariable Long id, Authentication authentication) {
        notificacionService.eliminarPorId(id);
        return "redirect:/productos/notificaciones";
    }


    private void agregarDatosUsuario(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }
    }
    @ModelAttribute
    public void agregarNotificacionesNoLeidas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            User usuario = userService.buscarPorEmail(auth.getName()).orElse(null);

            if (usuario != null) {
                List<Notificacion> noLeidas = notificacionService.obtenerNoLeidas(usuario);
                model.addAttribute("notificacionesNoLeidas", noLeidas);
            }
        }
    }
}
