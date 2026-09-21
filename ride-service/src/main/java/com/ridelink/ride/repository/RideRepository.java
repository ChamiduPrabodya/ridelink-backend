package com.ridelink.ride.repository;

import com.ridelink.ride.model.Ride;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride, Long> {
}
