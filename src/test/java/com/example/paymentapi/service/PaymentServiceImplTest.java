package com.example.paymentapi.service;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.model.PaymentRequest;
import com.example.paymentapi.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private PaymentRepository paymentRepository;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        paymentService = new PaymentServiceImpl(paymentRepository);
    }

    @Test
    void shouldReturnExistingPaymentIfIdempotencyKeyExists() {
        // given
        String key = "abc123";
        Payment existing = new Payment("client1", new BigDecimal("100.00"), "USD", key, "SUCCESS");

        when(paymentRepository.findByIdempotencyKey(key)).thenReturn(Optional.of(existing));

        // when
        Payment result = paymentService.processPayment(new PaymentRequest("client1", new BigDecimal("100.00"), "USD"), key);

        // then
        assertSame(existing, result);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldSaveAndReturnNewPaymentIfKeyNotFound() {
        // given
        String key = "unique-key";
        PaymentRequest request = new PaymentRequest("client2", new BigDecimal("200.00"), "EUR");

        when(paymentRepository.findByIdempotencyKey(key)).thenReturn(Optional.empty());

        Payment saved = new Payment("client2", new BigDecimal("200.00"), "EUR", key, "SUCCESS");
        when(paymentRepository.save(any())).thenReturn(saved);

        // when
        Payment result = paymentService.processPayment(request, key);

        // then
        assertEquals(saved, result);

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        Payment savedPayment = captor.getValue();

        assertEquals("client2", savedPayment.getClientId());
        assertEquals(new BigDecimal("200.00"), savedPayment.getAmount());
        assertEquals("EUR", savedPayment.getCurrency());
        assertEquals(key, savedPayment.getIdempotencyKey());
        assertEquals("SUCCESS", savedPayment.getStatus());
    }
}
