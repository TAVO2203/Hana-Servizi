package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Producto;
import com.example.hanaservizi_e.model.TallaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TallaStockRepository extends JpaRepository<TallaStock, Long> {
    Optional<TallaStock> findByProductoAndTalla(Producto producto, String talla);
}
