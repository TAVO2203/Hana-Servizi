package com.example.hanaservizi_e;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class UploadDirectoryConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void createUploadDirectory() {
        try {
            java.nio.file.Path uploadPath = Paths.get("C:/uploads/hana");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Directorio de uploads creado: " + uploadPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error creando directorio de uploads: " + e.getMessage());
        }
    }
}
