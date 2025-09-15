package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/register")
    public String create(Model model) {
        model.addAttribute("productoDto", new ProductoDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "registerProducto";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id){
        Producto p = productoRepository.findById(id).orElse(null);
        if(p != null){
            productoRepository.delete(p);
            logg.info("Objeto eliminado: {}", p);
        } else {
            logg.warn("No se encontró producto con id: {}", id);
        }
        return "redirect:/productos";
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
}
