package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.PerfilDto;
import com.example.hanaservizi_e.dto.ProductoDto;
import com.example.hanaservizi_e.dto.TallasStockDTO;
import com.example.hanaservizi_e.factory.ProductoFactory;
import com.example.hanaservizi_e.model.*;
import com.example.hanaservizi_e.service.CustomUserDetails;
import com.example.hanaservizi_e.service.PdfThymeleafService;
import com.example.hanaservizi_e.service.ProductoService;
import com.example.hanaservizi_e.service.UserService;
import com.example.hanaservizi_e.repository.CategoriaRepository;
import com.example.hanaservizi_e.repository.MarcaRepository;
import com.example.hanaservizi_e.repository.ProductoRepository;
import com.example.hanaservizi_e.service.impl.CustomUserDetailsImpl;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    private final Logger logg = LoggerFactory.getLogger(VendedorController.class);

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;
    private final UserService userService;
    private final PdfThymeleafService pdfThymeleafService;
    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoFactory productoFactory;

    // Constructor con inyección de dependencias (Spring lo hace automáticamente)
    public VendedorController(
            ProductoService productoService,
            ProductoRepository productoRepository,
            UserService userService,
            PdfThymeleafService pdfThymeleafService,
            MarcaRepository marcaRepository,
            CategoriaRepository categoriaRepository,
            ProductoFactory productoFactory
    ) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
        this.userService = userService;
        this.pdfThymeleafService = pdfThymeleafService;
        this.marcaRepository = marcaRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoFactory = productoFactory;
    }


    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User usuario = userDetails.getUsuario();
        model.addAttribute("usuario", usuario);
        return "vendedor/dashboard";
    }

    // Mostrar perfil del vendedor
    @GetMapping("/perfil")
    public String mostrarPerfil(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User usuario = userDetails.getUsuario();
        model.addAttribute("usuario", usuario);
        return "vendedor/perfil";
    }

    // Mostrar formulario de edición de perfil
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditar(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User usuario = userDetails.getUsuario();

        PerfilDto perfilDto = new PerfilDto();
        perfilDto.setUsername(usuario.getUsername());
        perfilDto.setEmail(usuario.getEmail());
        perfilDto.setPhone(usuario.getPhone());
        perfilDto.setAddress(usuario.getAddress());
        if (usuario.getVendedor() != null) perfilDto.setCity(usuario.getVendedor().getCity());

        model.addAttribute("perfilDto", perfilDto);
        model.addAttribute("usuario", usuario);

        return "vendedor/editarPerfilVendedor";
    }

    // Procesar edición de perfil
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(@Valid @ModelAttribute("perfilDto") PerfilDto perfilDto,
                                   BindingResult result,
                                   Model model,
                                   Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUsuario();

        if (result.hasErrors()) {
            model.addAttribute("usuario", user);
            return "vendedor/editarPerfilVendedor";
        }

        // 🔹 Actualizar datos básicos
        user.setUsername(perfilDto.getUsername());
        user.setEmail(perfilDto.getEmail());
        user.setPhone(perfilDto.getPhone());
        user.setAddress(perfilDto.getAddress());

        // 🔹 Campo especial city
        if (user.getVendedor() != null) {
            user.getVendedor().setCity(perfilDto.getCity());
        }

        // 🔹 Cambio de contraseña
        if (perfilDto.getCurrentPassword() != null && !perfilDto.getCurrentPassword().isBlank()) {
            if (!userService.verificarPassword(perfilDto.getCurrentPassword(), user.getPassword())) {
                model.addAttribute("usuario", user);
                model.addAttribute("error", "La contraseña actual es incorrecta.");
                return "vendedor/editarPerfilVendedor";
            }

            if (!perfilDto.getNewPassword().equals(perfilDto.getConfirmPassword())) {
                model.addAttribute("usuario", user);
                model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
                return "vendedor/editarPerfilVendedor";
            }

            user.setPassword(userService.codificarPassword(perfilDto.getNewPassword()));
        }

        userService.guardar(user);

        // 🔁 Refrescar autenticación
        CustomUserDetails nuevoUserDetails = new CustomUserDetailsImpl(user);
        UsernamePasswordAuthenticationToken nuevaAutenticacion =
                new UsernamePasswordAuthenticationToken(
                        nuevoUserDetails,
                        user.getPassword(),
                        nuevoUserDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(nuevaAutenticacion);

        return "redirect:/vendedor/perfil";
    }

    // Eliminar/Desactivar cuenta
    @PostMapping("/perfil/eliminar")
    public String eliminarCuenta(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User usuario = userDetails.getUsuario();

        userService.desactivarCuenta(usuario.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/login?cuentaDesactivada";
    }


    /* ===================== 📌 PRODUCTOS ===================== */


    @GetMapping("/productos")
    public String listarProductos(Authentication authentication,
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
    public String buscarProductos(@RequestParam(value = "keyword", required = false) String keyword,
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

    @GetMapping("/productos/agregar")
    public String mostrarFormularioAgregarProducto(Authentication authentication, Model model,
                                                   RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        model.addAttribute("productoDto", new ProductoDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("usuario", usuario);
        return "registerProducto";
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(
            @Valid @ModelAttribute("productoDto") ProductoDto productoDto,
            BindingResult result,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) throws Exception {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("usuario", usuario);
            return "registerProducto";
        }

        Producto producto = productoFactory.crearProducto(productoDto, usuario, null);
        producto = productoRepository.save(producto);

        // Guardar imagen con nombre único basado en ID
        if (productoDto.getImagenes() != null && !productoDto.getImagenes().isEmpty()) {
            MultipartFile archivo = productoDto.getImagenes().get(0);
            if (!archivo.isEmpty()) {
                String extension = archivo.getOriginalFilename()
                        .substring(archivo.getOriginalFilename().lastIndexOf("."));
                String nombreArchivo = "producto_" + producto.getId() + extension;

                Path ruta = Paths.get("C:/uploads/hana");
                if (!Files.exists(ruta)) Files.createDirectories(ruta);

                Files.copy(archivo.getInputStream(), ruta.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);

                producto.setImagen(nombreArchivo);
                productoRepository.save(producto);
            }
        }

        redirectAttributes.addFlashAttribute("success",
                "El producto '" + producto.getNombre() + "' se agregó correctamente.");

        return "redirect:/vendedor/productos";
    }


    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id,
                                           Authentication authentication,
                                           Model model) {

        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Producto producto = productoRepository.findById(id.intValue()).orElse(null);
        if (producto == null || !producto.getVendedor().equals(usuario)) {
            return "redirect:/vendedor/productos";
        }

        ProductoDto productoDto = new ProductoDto();
        productoDto.setId((int) producto.getId().longValue());
        productoDto.setNombre(producto.getNombre());
        productoDto.setPrecio(producto.getPrecio());
        productoDto.setDescripcion(producto.getDescripcion());
        productoDto.setEstadoProducto(producto.getEstadoProducto());
        productoDto.setFechaAgregacion(producto.getFechaAgregacion());
        productoDto.setStock(producto.getStock());
        if (producto.getCategorias() != null) productoDto.setCategoriaId(producto.getCategorias().getId());
        if (producto.getMarca() != null) productoDto.setNombreMarca(producto.getMarca().getNombreMarca());

        if (producto.getTallas() != null && !producto.getTallas().isEmpty()) {
            List<TallasStockDTO> tallasDto = producto.getTallas().stream()
                    .map(t -> new TallasStockDTO(t.getTalla(), t.getStock()))
                    .toList();
            productoDto.setTallas(tallasDto);

            int stockTotal = tallasDto.stream()
                    .mapToInt(TallasStockDTO::getStock)
                    .sum();
            productoDto.setStock(stockTotal);
        } else {
            productoDto.setStock(producto.getStock());
            productoDto.setTallas(new ArrayList<>());
        }


        model.addAttribute("productoDto", productoDto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("usuario", usuario);

        return "vendedor/edit";
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

        Producto producto = productoRepository.findById(productoDto.getId()).orElse(null);
        if (producto == null || !producto.getVendedor().equals(usuario)) {
            return "redirect:/vendedor/productos";
        }

        // Actualizar datos del producto
        productoFactory.actualizarProductoExistente(producto, productoDto, producto.getImagen());

        // Si hay nueva imagen, reemplazar la existente (mismo nombre)
        if (productoDto.getNuevaImagen() != null && !productoDto.getNuevaImagen().isEmpty()) {
            String extension = productoDto.getNuevaImagen().getOriginalFilename()
                    .substring(productoDto.getNuevaImagen().getOriginalFilename().lastIndexOf("."));
            String nombreArchivo = "producto_" + producto.getId() + extension;

            Path ruta = Paths.get("C:/uploads/hana");
            if (!Files.exists(ruta)) Files.createDirectories(ruta);

            Files.copy(productoDto.getNuevaImagen().getInputStream(),
                    ruta.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);

            producto.setImagen(nombreArchivo);
        }

        productoRepository.save(producto);
        return "redirect:/vendedor/productos";
    }


    @GetMapping("/productos/desactivar/{id}")
    public String desactivarProducto(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Producto producto = productoRepository.findById(Integer.valueOf(id.intValue())).orElse(null);

        if (producto != null && producto.getVendedor().equals(usuario)) {
            producto.setActivo(false);
            productoRepository.save(producto);
            logg.info("Producto desactivado: {}", producto.getNombre());
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

    @GetMapping("/productos/activar/{id}")
    public String activarProducto(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User usuario = userService.buscarPorEmail(email).orElse(null);

        Producto producto = productoRepository.findById(Integer.valueOf(id.intValue())).orElse(null); // usa intValue() si tu repo usa Integer

        if (producto != null && producto.getVendedor().equals(usuario)) {
            producto.setActivo(true);
            productoRepository.save(producto);
            logg.info("Producto reactivado: {}", producto.getNombre());
        } else {
            logg.warn("Producto no encontrado o no pertenece al vendedor. ID: {}", id);
        }

        return "redirect:/vendedor/productos";
    }

    @PostMapping("/productos/upload-csv")
    public String uploadCsv(
            @RequestParam("file") MultipartFile file,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        List<String> errores = new ArrayList<>();
        int productosCargados = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String linea;
            boolean primera = true;
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User usuario = userDetails.getUsuario();

            while ((linea = reader.readLine()) != null) {
                if (primera) { // saltar encabezado
                    primera = false;
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(",");

                if (datos.length < 8) { // ahora son 9 columnas (0–8)
                    errores.add("Fila inválida: " + linea);
                    continue;
                }

                try {
                    ProductoDto dto = new ProductoDto();
                    dto.setNombre(datos[0].trim());
                    dto.setPrecio(Double.parseDouble(datos[1].trim()));
                    dto.setFechaAgregacion(java.sql.Date.valueOf(datos[2].trim())); // yyyy-MM-dd
                    dto.setEstadoProducto(datos[3].trim());
                    dto.setDescripcion(datos[4].trim());
                    dto.setStock(Integer.parseInt(datos[5].trim()));

                    String categoriaNombre = datos[6].trim();
                    Categorias categoria = categoriaRepository.findByNombreCategoria(categoriaNombre)
                            .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoriaNombre));
                    dto.setCategoriaId(categoria.getId());

                    // --- Buscar o crear marca ---
                    String marcaNombre = datos[7].trim();
                    Marca marca = marcaRepository.findByNombreMarca(marcaNombre)
                            .orElseGet(() -> {
                                Marca nueva = new Marca();
                                nueva.setNombreMarca(marcaNombre);
                                return marcaRepository.save(nueva);
                            });
                    dto.setNombreMarca(marca.getNombreMarca());

                    Optional<Producto> productoExistente = productoRepository.findProductoDuplicado(
                            dto.getNombre(),
                            dto.getPrecio(),
                            dto.getCategoriaId(),
                            marca.getId(),
                            dto.getDescripcion()
                    );

                    if (productoExistente.isPresent()) {
                        errores.add("El producto '" + dto.getNombre() + "' ya existe.");
                        continue; // saltar este producto
                    }

                    // --- Tallas (solo si es Moda) ---
                    if (datos.length > 8 && datos[8] != null && !datos[8].isBlank()) {
                        if (!categoria.getNombreCategoria().equalsIgnoreCase("Moda")) {
                            errores.add("El producto '" + dto.getNombre() + "' no puede tener tallas porque es de la categoría " + categoria.getNombreCategoria());
                            continue;
                        }

                        String[] tallasSplit = datos[8].split(";");
                        List<TallasStockDTO> tallas = new ArrayList<>();
                        for (String t : tallasSplit) {
                            String[] kv = t.split(":");
                            if (kv.length == 2) {
                                tallas.add(new TallasStockDTO(kv[0].trim(), Integer.parseInt(kv[1].trim())));
                            }
                        }
                        dto.setTallas(tallas);
                    }

                    // Crear producto
                    Producto producto = productoFactory.crearProducto(dto, usuario, null);
                    productoRepository.save(producto);
                    productosCargados++;

                } catch (Exception e) {
                    errores.add("Error en fila: " + linea + " -> " + e.getMessage());
                }
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errores", List.of("Error al procesar CSV: " + e.getMessage()));
            return "redirect:/vendedor/productos";
        }

        if (!errores.isEmpty()) {
            redirectAttributes.addFlashAttribute("errores", errores);
        }
        if (productosCargados > 0) {
            redirectAttributes.addFlashAttribute("success", "Se cargaron " + productosCargados + " productos correctamente.");
        }

        return "redirect:/vendedor/productos";
    }


}
