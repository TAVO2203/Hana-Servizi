package com.example.hanaservizi_e.controller;

import com.example.hanaservizi_e.model.Categorias;
import com.example.hanaservizi_e.service.CategoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categorias")
public class AdminCategoriaController {

    private final CategoriaService categoriaService;

    public AdminCategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodo());
        model.addAttribute("categoriaNueva", new Categorias());
        return "admin/categorias";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categorias categoria) {
        categoriaService.guardar(categoria);
        return "redirect:/admin/categorias";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return "redirect:/admin/categorias";
    }
}
