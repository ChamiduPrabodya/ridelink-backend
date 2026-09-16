package com.ridelink.payment.service;

import org.springframework.stereotype.Service;

import com.ridelink.payment.dto.FareEstimateResponse;
import com.ridelink.payment.dto.FinalFareResponse;

@Service
public class FareService {

    private static final double BASE_FARE = 200.0;
    private static final double RATE_PER_KM = 80.0;

    public FareEstimateResponse calculateEstimatedFare(Double distanceKm) {

        double estimatedFare = BASE_FARE + (distanceKm * RATE_PER_KM);

        return new FareEstimateResponse(
                distanceKm,
                BASE_FARE,
                RATE_PER_KM,
                estimatedFare
        );
    }

    public FinalFareResponse calculateFinalFare(Double actualDistanceKm) {

        double finalFare = BASE_FARE + (actualDistanceKm * RATE_PER_KM);

        return new FinalFareResponse(
                actualDistanceKm,
                BASE_FARE,
                RATE_PER_KM,
                finalFare
        );
    }
}