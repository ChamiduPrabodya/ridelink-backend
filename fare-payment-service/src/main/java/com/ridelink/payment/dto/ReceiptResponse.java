package com.ridelink.payment.dto;

import java.time.LocalDateTime;

public class ReceiptResponse {

    private Long paymentId;
    private Long rideId;
    private Double amount;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    public ReceiptResponse(
            Long paymentId,
            Long rideId,
            Double amount,
            String paymentStatus,
            LocalDateTime paymentDate) {

        this.paymentId = paymentId;
        this.rideId = rideId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getRideId() {
        return rideId;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
}