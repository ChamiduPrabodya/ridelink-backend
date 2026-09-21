package com.ridelink.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FinalFareRequest {

    @NotNull(message = "Actual distance is required")
    @Positive(message = "Actual distance must be greater than 0")
    private Double actualDistanceKm;

    public Double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public void setActualDistanceKm(Double actualDistanceKm) {
        this.actualDistanceKm = actualDistanceKm;
    }
}
