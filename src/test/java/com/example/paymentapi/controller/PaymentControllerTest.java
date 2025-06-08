package com.example.paymentapi.controller;

import com.example.paymentapi.model.Payment;
import com.example.paymentapi.model.PaymentRequest;
import com.example.paymentapi.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;   // <-- NEW import
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Replaces the real PaymentService bean in the test ApplicationContext
     * with a Mockito mock.
     */
    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreatePaymentSuccessfully() throws Exception {
        PaymentRequest request = new PaymentRequest("client123", BigDecimal.valueOf(100), "USD");
        Payment saved = new Payment("client123", BigDecimal.valueOf(100), "USD", "key-123", "SUCCESS");

        when(paymentService.processPayment(any(), eq("key-123"))).thenReturn(saved);

        mockMvc.perform(post("/payments").header("Idempotency-Key", "key-123")
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.clientId").value("client123"))
               .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void shouldReturn400ForInvalidInput() throws Exception {
        PaymentRequest request = new PaymentRequest("", BigDecimal.valueOf(-50), "usd");

        mockMvc.perform(post("/payments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Idempotency-Key", "key-456")
                                .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.clientId").value("Client ID is required"))
               .andExpect(jsonPath("$.amount").value("Amount must be greater than 0"))
               .andExpect(jsonPath("$.currency").value("Currency must be a valid 3-letter ISO code"));
    }

}
