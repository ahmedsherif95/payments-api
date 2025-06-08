package com.example.paymentapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", uniqueConstraints = @UniqueConstraint(columnNames = "idempotencyKey"))
@Data
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private BigDecimal amount;
    private String currency;

    @Column(nullable = false, unique = true)
    private String idempotencyKey;

    private String status;

    private LocalDateTime createdAt;

    public Payment(String clientId, BigDecimal amount, String currency, String idempotencyKey, String status) {
        this.clientId = clientId;
        this.amount = amount;
        this.currency = currency;
        this.idempotencyKey = idempotencyKey;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

}
