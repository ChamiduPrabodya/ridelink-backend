package com.ridelink.ride;

import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public Ride requestRide(CreateRideRequest request) {
        Ride ride = new Ride();
        ride.setPassengerId(request.passengerId());
        ride.setPickupLocation(request.pickupLocation());
        ride.setDestinationLocation(request.destinationLocation());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setCreatedAt(Instant.now());
        ride.setUpdatedAt(Instant.now());
        return rideRepository.save(ride);
    }

    public Ride getRide(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
    }
}
