package com.example.paymentapi.repository;

import com.example.paymentapi.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}
