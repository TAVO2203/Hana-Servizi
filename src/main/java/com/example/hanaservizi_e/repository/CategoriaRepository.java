package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
    Optional<Categorias> findByNombreCategoria(String nombreCategoria);
}
