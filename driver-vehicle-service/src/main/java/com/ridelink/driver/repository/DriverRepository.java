package com.ridelink.driver.repository;

import com.ridelink.driver.enums.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {

    List<Driver> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);

    Optional<Driver> findByAccountId(String accountId);
}