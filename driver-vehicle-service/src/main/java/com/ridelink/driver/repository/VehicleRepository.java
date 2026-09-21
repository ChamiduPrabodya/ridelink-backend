package com.ridelink.driver.repository;

import com.ridelink.driver.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByDriverId(Long driverId);

    boolean existsByRegistrationNumber(String registrationNumber);
}
