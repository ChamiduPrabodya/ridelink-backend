package com.ridelink.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FareEstimateRequest {

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be greater than 0")
    private Double distanceKm;

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }
}