package com.ridelink.payment.dto;

public class FareEstimateResponse {

    private Double distanceKm;
    private Double baseFare;
    private Double ratePerKm;
    private Double estimatedFare;

    public FareEstimateResponse(
            Double distanceKm,
            Double baseFare,
            Double ratePerKm,
            Double estimatedFare) {
        this.distanceKm = distanceKm;
        this.baseFare = baseFare;
        this.ratePerKm = ratePerKm;
        this.estimatedFare = estimatedFare;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public Double getRatePerKm() {
        return ratePerKm;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }
}