package com.ridelink.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.ridelink.payment.dto.FareEstimateResponse;
import com.ridelink.payment.dto.FinalFareResponse;

class FareServiceTest {

    @Test
    void shouldCalculateEstimatedFareCorrectly() {

        FareService fareService = new FareService();

        FareEstimateResponse response =
                fareService.calculateEstimatedFare(10.0);

        assertEquals(10.0, response.getDistanceKm());
        assertEquals(200.0, response.getBaseFare());
        assertEquals(80.0, response.getRatePerKm());
        assertEquals(1000.0, response.getEstimatedFare());
    }

    @Test
    void shouldCalculateFinalFareCorrectly() {

        FareService fareService = new FareService();

        FinalFareResponse response =
                fareService.calculateFinalFare(12.0);

        assertEquals(12.0, response.getActualDistanceKm());
        assertEquals(200.0, response.getBaseFare());
        assertEquals(80.0, response.getRatePerKm());
        assertEquals(1160.0, response.getFinalFare());
    }
}