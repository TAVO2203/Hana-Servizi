package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.CustomOidcUser;
import com.example.hanaservizi_e.dto.PerfilDto;
import com.example.hanaservizi_e.model.User;
import com.example.hanaservizi_e.service.CustomUserDetails;
import com.example.hanaservizi_e.service.UserService;
import com.example.hanaservizi_e.service.impl.CustomUserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final UserService userService;

    public ClienteController(UserService userService) {
        this.userService = userService;
    }

    // Mostrar dashboard del cliente
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        Object principal = authentication.getPrincipal();
        User usuario;
        if (principal instanceof CustomUserDetails cud) {
            usuario = cud.getUsuario();
        } else if (principal instanceof CustomOidcUser coidc) {
            usuario = coidc.getUsuario();
        } else {
            throw new IllegalStateException("Tipo de usuario no soportado: " + principal.getClass());
        }

        model.addAttribute("usuario", usuario);
        return "cliente/dashboard";
    }


    // Mostrar perfil del cliente
    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User usuario = userDetails.getUsuario();
        model.addAttribute("usuario", usuario);
        return "cliente/perfil";
    }

    // Mostrar formulario para editar perfil
    @GetMapping("/editarPerfil")
    public String mostrarFormularioEditar(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User usuario = userDetails.getUsuario();

        PerfilDto perfilDto = new PerfilDto();
        perfilDto.setUsername(usuario.getUsername());
        perfilDto.setEmail(usuario.getEmail());
        perfilDto.setPhone(usuario.getPhone());
        perfilDto.setAddress(usuario.getAddress());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (email != null && !email.equals("anonymousUser")) {
            User user = userService.buscarPorEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("nombreUsuario", user.getUsername());
                model.addAttribute("rolUsuario", user.getRol().getRolname());
            }
        }
        model.addAttribute("perfilDto", perfilDto);
        model.addAttribute("usuario", usuario);

        return "cliente/editarPerfilCliente";
    }

    // Procesar edición
    @PostMapping("/editarPerfil")
    public String actualizarPerfil(@Valid @ModelAttribute("perfilDto") PerfilDto perfilDto,
                                   BindingResult result,
                                   Model model,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUsuario();

        if (result.hasErrors()) {
            model.addAttribute("usuario", user);
            return "cliente/editarPerfilCliente";
        }

        // 🔹 Actualizar datos básicos
        user.setUsername(perfilDto.getUsername());
        user.setEmail(perfilDto.getEmail());
        user.setPhone(perfilDto.getPhone());
        user.setAddress(perfilDto.getAddress());

        // 🔹 Cambio de contraseña (solo si aplica)
        if (perfilDto.getCurrentPassword() != null && !perfilDto.getCurrentPassword().isBlank()) {
            if (!userService.verificarPassword(perfilDto.getCurrentPassword(), user.getPassword())) {
                model.addAttribute("usuario", user);
                model.addAttribute("error", "La contraseña actual es incorrecta.");
                return "cliente/editarPerfilCliente";
            }

            if (!perfilDto.getNewPassword().equals(perfilDto.getConfirmPassword())) {
                model.addAttribute("usuario", user);
                model.addAttribute("error", "Las contraseñas nuevas no coinciden.");
                return "cliente/editarPerfilCliente";
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

        return "redirect:/cliente/perfil";
    }

    @PostMapping("/perfil/eliminar")
    public String desactivarCuenta(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        User usuario = userDetails.getUsuario();

        // Desactivar en la BD
        userService.desactivarCuenta(usuario.getId());

        // Limpiar autenticación de Spring Security
        SecurityContextHolder.clearContext();

        // Invalidar sesión HTTP
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirigir al login
        return "redirect:/login?cuentaDesactivada";
    }

}
