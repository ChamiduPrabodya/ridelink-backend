package com.ridelink.payment.dto;

import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long rideId;
    private Double amount;
    private String status;
    private LocalDateTime createdAt;

    public PaymentResponse(
            Long id,
            Long rideId,
            Double amount,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.rideId = rideId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getRideId() {
        return rideId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}