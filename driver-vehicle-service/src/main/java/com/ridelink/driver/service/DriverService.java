package com.ridelink.driver.service;

import com.ridelink.driver.dto.AvailabilityRequest;
import com.ridelink.driver.dto.DriverRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.LocationRequest;
import com.ridelink.driver.enums.AvailabilityStatus;
import com.ridelink.driver.exception.DriverNotFoundException;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    // Create driver
    public DriverResponse createDriver(DriverRequest request) {

        Driver driver = new Driver();

        driver.setAccountId(request.getAccountId());
        driver.setServiceArea(request.getServiceArea());
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());
        driver.setAvailabilityStatus(request.getAvailabilityStatus());

        Driver savedDriver = driverRepository.save(driver);

        return mapToResponse(savedDriver);
    }

    // Get driver by ID
    public DriverResponse getDriverById(Long id) {

        Driver driver = findDriver(id);

        return mapToResponse(driver);
    }

    // Get all drivers
    public List<DriverResponse> getAllDrivers() {

        return driverRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Update driver profile
    public DriverResponse updateDriver(Long id, DriverRequest request) {

        Driver driver = findDriver(id);

        driver.setAccountId(request.getAccountId());
        driver.setServiceArea(request.getServiceArea());
        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());
        driver.setAvailabilityStatus(request.getAvailabilityStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponse(updatedDriver);
    }

    // Update availability
    public DriverResponse updateAvailability(
            Long id,
            AvailabilityRequest request) {

        Driver driver = findDriver(id);

        driver.setAvailabilityStatus(request.getStatus());

        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponse(updatedDriver);
    }

    // Update simulated location
    public DriverResponse updateLocation(
            Long id,
            LocationRequest request) {

        Driver driver = findDriver(id);

        driver.setLatitude(request.getLatitude());
        driver.setLongitude(request.getLongitude());

        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponse(updatedDriver);
    }

    // Get all available drivers
    public List<DriverResponse> getAvailableDrivers() {

        return driverRepository
                .findByAvailabilityStatus(AvailabilityStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Find driver internally
    private Driver findDriver(Long id) {

        return driverRepository.findById(id)
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "Driver not found with ID: " + id
                        ));
    }

    // Convert Driver Entity -> DriverResponse DTO
    private DriverResponse mapToResponse(Driver driver) {

        return new DriverResponse(
                driver.getId(),
                driver.getAccountId(),
                driver.getServiceArea(),
                driver.getLatitude(),
                driver.getLongitude(),
                driver.getAvailabilityStatus()
        );
    }
}
