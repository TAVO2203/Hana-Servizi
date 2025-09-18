package com.example.hanaservizi_e.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;
    private boolean leido = false;
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User usuario;

}

