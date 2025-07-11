package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.model.Marca;
import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.service.PdfThymeleafService;
import com.example.hanaservizi_e.service.ProductoService;
import com.example.hanaservizi_e.service.UserService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    private final Logger logg = LoggerFactory.getLogger(VendedorController.class);

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PdfThymeleafService pdfThymeleafService;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;


    @GetMapping("/dashboard")
    public String mostrarDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);
        model.addAttribute("usuario", usuario);
        return "vendedor/dashboard";
    }


    @GetMapping("/productos")
    public String listarProductos(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Model model) {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Page<Producto> productosPage = productoService.listarProductosPorVendedor(
                usuario.getId(), PageRequest.of(page, size));

        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("page", productosPage);
        model.addAttribute("keyword", "");
        model.addAttribute("urlBase", "/vendedor/productos/buscar");
        model.addAttribute("usuario", usuario);

        return "vendedor/productos";
    }

    @GetMapping("/productos/buscar")
    public String buscarProductos(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            Authentication authentication,
            Model model) {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Page<Producto> productosPage;
        if (keyword != null && !keyword.isBlank()) {
            productosPage = productoService.buscarProductosPorVendedorYKeyword(
                    keyword, usuario.getId(), PageRequest.of(page, size));
        } else {
            productosPage = productoService.listarProductosPorVendedor(
                    usuario.getId(), PageRequest.of(page, size));
        }

        model.addAttribute("productos", productosPage.getContent());
        model.addAttribute("page", productosPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("urlBase", "/vendedor/productos/buscar");
        model.addAttribute("usuario", usuario);

        return "vendedor/productos";
    }

    /**
     * Formulario para agregar producto
     */
    @GetMapping("/productos/agregar")
    public String mostrarFormularioAgregarProducto(Authentication authentication, Model model) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        model.addAttribute("productoDto", new ProductoDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("usuario", usuario);
        return "registerProducto";
    }

    @PostMapping("/productos/editar")
    public String actualizarProducto(
            @Valid @ModelAttribute("productoDto") ProductoDto productoDto,
            BindingResult result,
            Authentication authentication,
            Model model) throws Exception {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("usuario", usuario);
            return "vendedor/edit";
        }

        Producto producto = productoRepository.findById(productoDto.getId().intValue()).orElse(null);
        if (producto == null || !producto.getVendedor().equals(usuario)) {
            return "redirect:/vendedor/productos";
        }

        // Actualiza los datos
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setEstadoProducto(productoDto.getEstadoProducto());
        producto.setFechaAgregacion(productoDto.getFechaAgregacion());

        if (productoDto.getCategoriaId() != null) {
            Categorias categoria = categoriaRepository.findById(productoDto.getCategoriaId()).orElse(null);
            producto.setCategorias(categoria);
        }

        if (productoDto.getNombreMarca() != null && !productoDto.getNombreMarca().isBlank()) {
            Marca marca = marcaRepository.findByNombreMarca(productoDto.getNombreMarca())
                    .orElseGet(() -> marcaRepository.save(new Marca(productoDto.getNombreMarca())));
            producto.setMarca(marca);
        }

        if (productoDto.getImagen() != null && !productoDto.getImagen().isEmpty()) {
            String nombreArchivo = productoDto.getImagen().getOriginalFilename();
            Path ruta = Paths.get("C:/uploads/hana");
            if (!Files.exists(ruta)) Files.createDirectories(ruta);
            productoDto.getImagen().transferTo(ruta.resolve(nombreArchivo).toFile());
            producto.setImagen(nombreArchivo);
        }

        productoRepository.save(producto);
        return "redirect:/vendedor/productos";

    }


    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("productoDto") ProductoDto productoDto,
            BindingResult result,
            Authentication authentication,
            Model model) throws Exception {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("usuario", usuario);
            return "registerProducto";
        }

        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setPrecio(productoDto.getPrecio());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setEstadoProducto(productoDto.getEstadoProducto());
        producto.setFechaAgregacion(productoDto.getFechaAgregacion());
        producto.setVendedor(usuario);

        if (productoDto.getCategoriaId() != null) {
            Categorias categoria = categoriaRepository.findById(productoDto.getCategoriaId()).orElse(null);
            producto.setCategorias(categoria);
        }

        if (productoDto.getNombreMarca() != null && !productoDto.getNombreMarca().isBlank()) {
            Marca marca = marcaRepository.findByNombreMarca(productoDto.getNombreMarca())
                    .orElseGet(() -> marcaRepository.save(new Marca(productoDto.getNombreMarca())));
            producto.setMarca(marca);
        }

        if (productoDto.getImagen() != null && !productoDto.getImagen().isEmpty()) {
            String nombreArchivo = productoDto.getImagen().getOriginalFilename();
            Path ruta = Paths.get("C:/uploads/hana");
            if (!Files.exists(ruta)) Files.createDirectories(ruta);
            productoDto.getImagen().transferTo(ruta.resolve(nombreArchivo).toFile());
            producto.setImagen(nombreArchivo);
        }

        productoRepository.save(producto);
        return "redirect:/vendedor/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Producto producto = productoRepository.findById(id.intValue()).orElse(null);

        if (producto != null && producto.getVendedor().equals(usuario)) {
            productoRepository.delete(producto);
            logg.info("Producto eliminado: {}", producto.getNombre());
        } else {
            logg.warn("Producto no encontrado o no pertenece al vendedor. ID: {}", id);
        }

        return "redirect:/vendedor/productos";
    }


    @GetMapping("/productos/reporte-pdf")
    public ResponseEntity<ByteArrayResource> generarReportePdf(Authentication authentication) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);
        List<Producto> productos = productoRepository.findByVendedor(usuario);

        byte[] pdf = pdfThymeleafService.generarPdfDesdeHtml(productos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=mis_productos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }
    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Producto producto = productoRepository.findById(id.intValue()).orElse(null);
        if (producto == null || !producto.getVendedor().equals(usuario)) {
            return "redirect:/vendedor/productos";
        }

        // Cargar los datos del producto en el DTO
        ProductoDto productoDto = new ProductoDto();
        productoDto.setId(producto.getId().longValue());
        productoDto.setNombre(producto.getNombre());
        productoDto.setPrecio(producto.getPrecio());
        productoDto.setDescripcion(producto.getDescripcion());
        productoDto.setEstadoProducto(producto.getEstadoProducto());
        productoDto.setFechaAgregacion(producto.getFechaAgregacion());
        if (producto.getCategorias() != null)
            productoDto.setCategoriaId(producto.getCategorias().getId());
        if (producto.getMarca() != null)
            productoDto.setNombreMarca(producto.getMarca().getNombreMarca());

        model.addAttribute("productoDto", productoDto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("usuario", usuario);

        return "vendedor/edit";
    }

}