package com.example.paymentapi.service;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.model.PaymentRequest;
import com.example.paymentapi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment processPayment(PaymentRequest request, String idempotencyKey) {
        Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existing.isPresent()) {
            return existing.get();
        }

        Payment newPayment = new Payment(
                request.getClientId(),
                request.getAmount(),
                request.getCurrency(),
                idempotencyKey,
                "SUCCESS"
        );

        return paymentRepository.save(newPayment);
    }
}

