package com.example.hanaservizi_e.repository;


import com.example.hanaservizi_e.model.Pago;
import com.example.hanaservizi_e.model.Pedido;
import com.example.hanaservizi_e.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteOrderByFechaDesc(User cliente);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.items WHERE p.cliente = :cliente ORDER BY p.fecha DESC")
    List<Pedido> findByClienteWithItems(@Param("cliente") User cliente);

    Optional<Pedido> findByPago(Pago pago);
}

