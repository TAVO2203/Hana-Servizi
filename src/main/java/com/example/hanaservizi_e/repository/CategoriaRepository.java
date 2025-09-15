package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Categorias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categorias, Long> {
    Optional<Categorias> findByNombreCategoria(String nombreCategoria);

    @Query(value = "SELECT * FROM categorias", nativeQuery = true)
    List<Categorias> findAllByOrderByIdAsc();


}
