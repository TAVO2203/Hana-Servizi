package com.example.hanaservizi_e.repository;

import com.example.hanaservizi_e.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByStripePaymentId(String stripePaymentId);
}
