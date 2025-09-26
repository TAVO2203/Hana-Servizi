package com.example.hanaservizi_e.service;

import com.example.hanaservizi_e.model.Pedido;
import com.example.hanaservizi_e.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<Pedido> listarTodos() {
        // Trae todos los pedidos con items para que no haga lazy loading en la vista
        return pedidoRepository.findAll();
    }

    public Pedido actualizarEstado(Long id, String estado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}

