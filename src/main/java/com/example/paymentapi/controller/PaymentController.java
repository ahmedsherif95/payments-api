package com.example.paymentapi.controller;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.model.PaymentRequest;
import com.example.paymentapi.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @RequestBody @Valid PaymentRequest paymentRequest,
            @RequestHeader("Idempotency-Key") String idempotencyKey) {

        Payment result = paymentService.processPayment(paymentRequest,idempotencyKey);
        return ResponseEntity.ok(result);

    }
}
