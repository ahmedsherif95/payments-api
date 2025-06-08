package com.example.paymentapi.service;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.model.PaymentRequest;

public interface PaymentService {
    Payment processPayment(PaymentRequest request, String idempotencyKey);
}
