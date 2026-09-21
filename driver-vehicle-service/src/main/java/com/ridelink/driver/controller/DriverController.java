package com.ridelink.driver.controller;

import com.ridelink.driver.dto.AvailabilityRequest;
import com.ridelink.driver.dto.DriverRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.LocationRequest;
import com.ridelink.driver.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    // Create driver
    @PostMapping
    public ResponseEntity<DriverResponse> createDriver(
            @Valid @RequestBody DriverRequest request) {

        DriverResponse response = driverService.createDriver(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Get all drivers
    @GetMapping
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {

        return ResponseEntity.ok(
                driverService.getAllDrivers()
        );
    }

    // Get driver by ID
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> getDriverById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                driverService.getDriverById(id)
        );
    }

    // Update driver
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable String id,
            @Valid @RequestBody DriverRequest request) {

        return ResponseEntity.ok(
                driverService.updateDriver(id, request)
        );
    }

    // Update availability
    @PutMapping("/{id}/availability")
    public ResponseEntity<DriverResponse> updateAvailability(
            @PathVariable String id,
            @Valid @RequestBody AvailabilityRequest request) {

        return ResponseEntity.ok(
                driverService.updateAvailability(id, request)
        );
    }

    // Update simulated location
    @PutMapping("/{id}/location")
    public ResponseEntity<DriverResponse> updateLocation(
            @PathVariable String id,
            @Valid @RequestBody LocationRequest request) {

        return ResponseEntity.ok(
                driverService.updateLocation(id, request)
        );
    }

    // Get available drivers
    @GetMapping("/available")
    public ResponseEntity<List<DriverResponse>> getAvailableDrivers() {

        return ResponseEntity.ok(
                driverService.getAvailableDrivers()
        );
    }
}