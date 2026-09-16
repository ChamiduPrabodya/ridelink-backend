package com.ridelink.payment.dto;

public class FinalFareResponse {

    private Double actualDistanceKm;
    private Double baseFare;
    private Double ratePerKm;
    private Double finalFare;

    public FinalFareResponse(
            Double actualDistanceKm,
            Double baseFare,
            Double ratePerKm,
            Double finalFare) {
        this.actualDistanceKm = actualDistanceKm;
        this.baseFare = baseFare;
        this.ratePerKm = ratePerKm;
        this.finalFare = finalFare;
    }

    public Double getActualDistanceKm() {
        return actualDistanceKm;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public Double getRatePerKm() {
        return ratePerKm;
    }

    public Double getFinalFare() {
        return finalFare;
    }
}