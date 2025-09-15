package com.example.hanaservizi_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DesactiveController {

    @GetMapping("/cuenta-desactivada")
        public String cuentaDesactivada() {
            return "cuentaDesactivada"; // Thymeleaf
        }
    }

