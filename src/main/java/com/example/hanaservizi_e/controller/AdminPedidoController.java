package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.model.Pedido;
import com.example.hanaservizi_e.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public AdminPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Listar todos los pedidos
    @GetMapping
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = pedidoService.listarTodos();
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("estados", List.of("Pendiente", "Pagado", "En Preparación", "Enviado", "Entregado"));
        return "admin/pedidos"; // vista: src/main/resources/templates/admin/pedidos.html
    }

    // Cambiar estado de un pedido
    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        pedidoService.actualizarEstado(id, estado);
        return "redirect:/admin/pedidos";
    }
}

