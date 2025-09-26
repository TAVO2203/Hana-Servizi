package com.example.hanaservizi_e.Items;

import com.example.hanaservizi_e.model.Pedido;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreProducto;
    private String talla;
    private int cantidad;
    private Double precio;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

}

