package com.ridelink.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.payment.dto.FareEstimateRequest;
import com.ridelink.payment.dto.FareEstimateResponse;
import com.ridelink.payment.dto.FinalFareRequest;
import com.ridelink.payment.dto.FinalFareResponse;
import com.ridelink.payment.service.FareService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping("/estimate")
    public ResponseEntity<FareEstimateResponse> estimateFare(
            @Valid @RequestBody FareEstimateRequest request) {

        FareEstimateResponse response =
                fareService.calculateEstimatedFare(request.getDistanceKm());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/final")
    public ResponseEntity<FinalFareResponse> calculateFinalFare(
            @Valid @RequestBody FinalFareRequest request) {

        FinalFareResponse response =
                fareService.calculateFinalFare(request.getActualDistanceKm());

        return ResponseEntity.ok(response);
    }
}