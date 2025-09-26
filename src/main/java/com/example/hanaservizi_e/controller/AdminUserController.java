package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.dto.RegistroUsuarioDTO;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.model.Rol;
import com.example.hanaservizi_e.repository.UserRepository;
import com.example.hanaservizi_e.service.EmailService;
import com.example.hanaservizi_e.service.NotificacionService;
import com.example.hanaservizi_e.service.ReportePdfService;
import com.example.hanaservizi_e.service.UserService;
import com.example.hanaservizi_e.repository.RolRepository;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUserController {

    private final UserService userService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public AdminUserController(UserService userService, RolRepository rolRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, EmailService emailMasivoService) {
        this.userService = userService;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailService = emailMasivoService;
    }

    private void agregarAdminAlModelo(Authentication authentication, Model model) {
        String email = authentication.getName();
        User admin = userService.buscarPorEmail(email).orElse(null);
        model.addAttribute("admin", admin);


    }

    // Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Authentication authentication, Model model) {
        agregarAdminAlModelo(authentication, model);
        List<User> usuarios = userService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "admin/lista_usuarios";
    }

    //Filtrar usuarios//
    @GetMapping("/filtrar")
    public String filtrarUsuario(@RequestParam String tipo,
                                 @RequestParam String valor,
                                 Authentication authentication,
                                 Model model) {
        agregarAdminAlModelo(authentication, model);
        List<User> usuarios;

        switch (tipo) {
            case "nombre":
                Optional<User> usuarioPorNombre = userService.buscarPorNombre(valor);
                usuarios = usuarioPorNombre.map(List::of).orElse(List.of());
                break;
            case "email":
                Optional<User> usuarioPorEmail = userService.buscarPorEmail(valor);
                usuarios = usuarioPorEmail.map(List::of).orElse(List.of());
                break;
            case "direcion":
                usuarios = userService.buscarPorDireccion(valor);
                break;
            case "rol":
                usuarios = userService.listarPorRol("ROLE_" + valor.toUpperCase());
                break;
            case "todos":
            default:
                usuarios = userService.listarTodos();
        }

        model.addAttribute("usuarios", usuarios);
        if (usuarios.isEmpty()) {
            model.addAttribute("mensaje", "No se encontraron usuarios con esos datos.");
        }
        return "admin/lista_usuarios";

    }

    // Mostrar formulario para agregar nuevo admin
    @GetMapping("/nuevo-admin")
    public String mostrarFormularioNuevoAdmin(Authentication authentication, Model model) {
        agregarAdminAlModelo(authentication, model);
        model.addAttribute("usuario", new RegistroUsuarioDTO());
        return "admin/form_nuevo_admin";
    }

    // Procesar creación de nuevo admin
    @PostMapping("/nuevo-admin")
    public String crearNuevoAdmin(@Valid @ModelAttribute("usuario") RegistroUsuarioDTO userDto,
                                  BindingResult result,
                                  Authentication authentication,
                                  Model model) {
        agregarAdminAlModelo(authentication, model);
        if (result.hasErrors()) {

            return "admin/form_nuevo_admin";
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "admin/form_nuevo_admin";
        }
        try {
            Rol adminrol = rolRepository.findByRolname("ROLE_ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Rol ADMIN no configurado"));

            //Crear usuario desde DTO//
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setRol(adminrol);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            userService.registrarUsuario(user);
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("error", "No se puede crear el nuevo administrador: " + e.getMessage());
            return "admin/form_nuevo_admin";
        }
    }

    @PostMapping("/cambiar-rol/{id}")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam("nuevoRol") String nuevoRol) {
        User usuario = userService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Rol rol = rolRepository.findByRolname("ROLE_" + nuevoRol.toUpperCase())
                .orElseThrow(() -> new IllegalStateException("Rol no encontrado"));

        usuario.setRol(rol);
        userService.guardar(usuario);

        return "redirect:/admin/usuarios";
    }


    @Autowired
    private ReportePdfService reportePdfService;

    @GetMapping("/reporte-pdf")
    public void descargarReporteUsuarios(Authentication authentication, HttpServletResponse response) {
        try {
            List<User> usuarios = userService.listarTodos();
            byte[] pdfBytes = reportePdfService.generarReporteUsuarios(usuarios);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.pdf");
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/estadisticas")
    public String verEstadisticas(Model model) {
        long totalUsuarios = userRepository.count();

        // traer los usuarios agrupados por mes
        Map<String, Long> usuariosPorMes = userService.obtenerUsuariosPorMes();

        // asegurar que salgan los 12 meses, aunque estén en 0
        String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};

        for (String mes : meses) {
            usuariosPorMes.putIfAbsent(mes, 0L);
        }

        model.addAttribute("activePage", "dashboard");
        model.addAttribute("totalUsuarios", totalUsuarios);
        model.addAttribute("labels", usuariosPorMes.keySet()); // meses
        model.addAttribute("data", usuariosPorMes.values());   // cantidades

        return "admin/verEstadisticas";
    }

    @PostMapping("/estadisticas/reporte-pdf")
    public void descargarReporteEstadisticas(
            @RequestParam("graficoBase64") String graficoBase64,
            HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=estadisticas.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            document.add(new Paragraph("📊 Reporte de Estadísticas"));
            document.add(new Paragraph("Generado: " + java.time.LocalDateTime.now()));
            document.add(new Paragraph(" "));


            String base64Image = graficoBase64.split(",")[1];
            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Image);
            com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(imageBytes);

            image.scaleToFit(500, 300);
            document.add(image);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/mensajes-masivos")
    public String mostrarFormularioMensajes(Authentication authentication, Model model) {
        System.out.println("MOSTRANDO FORMULARIO DE CORREOS MASIVOS");
        agregarAdminAlModelo(authentication, model);

        return "admin/correosMasivos";
    }
    @Autowired
    private NotificacionService notificacionService;

    @PostMapping("/enviar-correo-masivo")
    public String enviarCorreoMasivo(
            @RequestParam("asunto") String asunto,
            @RequestParam("mensaje") String mensaje,
            Model model,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        agregarAdminAlModelo(authentication, model);

        try {
            List<User> usuarios = userService.listarTodos();

            // enviar correos
            emailService.enviarCorreoATodos(asunto, mensaje);

            // registrar notificaciones
            for (User usuario : usuarios) {
                notificacionService.crearNotificacion(usuario, asunto, mensaje);
            }

            redirectAttributes.addFlashAttribute("success", "✅ Correos enviados y notificaciones creadas.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "❌ Error: " + e.getMessage());
        }

        return "admin/correosMasivos";
    }


}

