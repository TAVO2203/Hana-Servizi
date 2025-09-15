package com.example.hanaservizi_e.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied"; // busca templates/access_denied.html
    }
}
