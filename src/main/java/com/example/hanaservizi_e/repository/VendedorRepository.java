package com.example.hanaservizi_e.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hanaservizi_e.model.Vendedor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor,Long> {
    //Filters//
    Optional<Vendedor> findByCity(String city);
}
