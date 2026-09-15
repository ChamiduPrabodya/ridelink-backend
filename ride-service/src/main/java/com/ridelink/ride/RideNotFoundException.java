package com.ridelink.ride;

public class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(Long id) {
        super("Ride not found: " + id);
    }
}
