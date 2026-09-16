package com.ridelink.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ridelink.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}