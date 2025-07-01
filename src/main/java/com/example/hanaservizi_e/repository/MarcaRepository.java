package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombreMarca(String nombreMarca);
}
