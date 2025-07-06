package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.service.PdfThymeleafService;
import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.model.Marca;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        List<Producto> todos = productoRepository.findAll();
        Collections.shuffle(todos);

        List<Producto> productosAleatorios = todos.stream()
                .limit(6)
                .toList();

        model.addAttribute("productos", productosAleatorios);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }

        return "index";
    }


    @GetMapping("")
    public String home(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Producto> productosPage;

        if (keyword != null && !keyword.isEmpty()) {
            productosPage = productoRepository.buscarPorTodosLosCampos("%" + keyword.toLowerCase() + "%", pageable);
            model.addAttribute("keyword", keyword);
            model.addAttribute("urlBase", "/productos/buscar");
        } else {
            productosPage = productoRepository.findAll(pageable);
            model.addAttribute("urlBase", "/productos");
        }

        model.addAttribute("page", productosPage);
        return "home";
    }


    @GetMapping("/register")
    public String create(Model model) {
        model.addAttribute("productoDto", new ProductoDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "registerProducto";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("productoDto") ProductoDto productoDto,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            return (productoDto.getId() != null) ? "edit" : "registerProducto";
        }


        Producto producto;

        if (productoDto.getId() != null && productoRepository.existsById(productoDto.getId().intValue())) {
            producto = productoRepository.getById(productoDto.getId().intValue());
        }else{
            producto = new Producto();
        }


        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setFechaAgregacion(productoDto.getFechaAgregacion());
        producto.setEstadoProducto(productoDto.getEstadoProducto());
        producto.setDescripcion(productoDto.getDescripcion());


        // Guardar imagen
        MultipartFile imagen = productoDto.getImagen();
        String ruta = "C:/uploads/hana/";

        try {
            if (imagen != null && !imagen.isEmpty()) {
                byte[] bytes = imagen.getBytes();
                Path rutaCompleta = Paths.get(ruta + imagen.getOriginalFilename());
                Files.write(rutaCompleta, bytes);

                producto.setImagen(imagen.getOriginalFilename());

                logg.info("Imagen guardada correctamente: {}", producto.getImagen());
            }
        } catch (IOException e) {
            logg.error("Error al guardar la imagen: {}", e.getMessage());
            e.printStackTrace();
        }


        Marca marca = marcaRepository.findByNombreMarca(productoDto.getNombreMarca())
                .orElseGet(() -> {
                    Marca nueva = new Marca();
                    nueva.setNombreMarca(productoDto.getNombreMarca());
                    return marcaRepository.save(nueva);
                });

        Categorias categoria = categoriaRepository.findById(productoDto.getCategoriaId()).orElse(null);

        producto.setMarca(marca);
        producto.setCategorias(categoria);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User vendedor = userService.buscarPorEmail(email).orElse(null);

        producto.setVendedor(vendedor);
        productoRepository.save(producto);

        return "redirect:/productos";





    }


    @GetMapping("/edit/{id}")
    public String editProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductoDto dto = new ProductoDto();
        dto.setId(producto.getId().longValue());
        dto.setNombre(producto.getNombre());
        dto.setPrecio(producto.getPrecio());
        dto.setFechaAgregacion(producto.getFechaAgregacion());
        dto.setEstadoProducto(producto.getEstadoProducto());
        dto.setDescripcion(producto.getDescripcion());
        dto.setNombreMarca(producto.getMarca() != null ? producto.getMarca().getNombreMarca() : null);
        dto.setCategoriaId(producto.getCategorias() != null ? producto.getCategorias().getId().longValue() : null);

        model.addAttribute("productoDto", dto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        dto.setImagenActual(producto.getImagen());

        return "vendedor/edit";
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

        List<Producto> productos = productoRepository.findByCategoriasId(id);

        for (Producto producto : productos) {
            if (producto.getImagen() != null && !producto.getImagen().startsWith("/uploads/")) {
                producto.setImagen("/uploads/" + producto.getImagen());
            }
        }

        // BUSCAR LA CATEGORIA:
        Optional<Categorias> categoriaOpt = categoriaRepository.findById(id);
        String nombreCategoria = categoriaOpt.map(Categorias::getNombreCategoria).orElse("Categoría no encontrada");

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaNombre", nombreCategoria);

        return "FiltrosCategorias";
    }
    @GetMapping("/productos/ver/{id}")
    public String verProducto(@PathVariable Long id, Model model) {
        Optional<Producto> producto = productoRepository.findById(Math.toIntExact(id));
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "detalle";
        } else {
            return "redirect:/productos";
        }
    }



}