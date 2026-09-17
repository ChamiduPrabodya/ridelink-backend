package com.ridelink.payment.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import com.ridelink.payment.dto.PaymentRequest;
import com.ridelink.payment.dto.PaymentResponse;
import com.ridelink.payment.dto.ReceiptResponse;
import com.ridelink.payment.entity.Payment;
import com.ridelink.payment.repository.PaymentRepository;

class PaymentServiceTest {

    @Test
    void shouldRecordPaymentSuccessfully() {

        PaymentRepository paymentRepository =
                Mockito.mock(PaymentRepository.class);

        PaymentService paymentService =
                new PaymentService(paymentRepository);

        PaymentRequest request = new PaymentRequest();
        request.setRideId(101L);
        request.setAmount(1160.0);

        Payment savedPayment =
                new Payment(101L, 1160.0, "PAID");

        when(paymentRepository.save(any(Payment.class)))
                .thenReturn(savedPayment);

        PaymentResponse response =
                paymentService.recordPayment(request);

        assertEquals(101L, response.getRideId());
        assertEquals(1160.0, response.getAmount());
        assertEquals("PAID", response.getStatus());
    }

    @Test
    void shouldRetrievePaymentById() {

        PaymentRepository paymentRepository =
                Mockito.mock(PaymentRepository.class);

        PaymentService paymentService =
                new PaymentService(paymentRepository);

        Payment payment =
                new Payment(101L, 1160.0, "PAID");

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        PaymentResponse response =
                paymentService.getPaymentById(1L);

        assertEquals(101L, response.getRideId());
        assertEquals(1160.0, response.getAmount());
        assertEquals("PAID", response.getStatus());
    }

    @Test
    void shouldGenerateReceiptForPayment() {

        PaymentRepository paymentRepository =
                Mockito.mock(PaymentRepository.class);

        PaymentService paymentService =
                new PaymentService(paymentRepository);

        Payment payment =
                new Payment(101L, 1160.0, "PAID");

        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));

        ReceiptResponse response =
                paymentService.getReceipt(1L);

        assertEquals(101L, response.getRideId());
        assertEquals(1160.0, response.getAmount());
        assertEquals("PAID", response.getPaymentStatus());
    }
}