package com.example.hanaservizi_e.model;

import com.example.hanaservizi_e.Items.ItemPedido;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Double total;
    private String estado; // Ej: "Pendiente", "Enviado", "Entregado"

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> items;

    @ManyToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;

}

