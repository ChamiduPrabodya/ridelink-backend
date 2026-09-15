package com.ridelink.driver.service;

import com.ridelink.driver.dto.VehicleRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.exception.DriverNotFoundException;
import com.ridelink.driver.exception.VehicleNotFoundException;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
    }

    // Add vehicle
    public VehicleResponse addVehicle(VehicleRequest request) {

        // Check driver exists
        if (!driverRepository.existsById(request.getDriverId())) {
            throw new DriverNotFoundException(
                    "Driver not found with ID: " + request.getDriverId()
            );
        }

        // Check duplicate registration number
        if (vehicleRepository.existsByRegistrationNumber(
                request.getRegistrationNumber())) {

            throw new IllegalArgumentException(
                    "Vehicle registration number already exists"
            );
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setDriverId(request.getDriverId());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setColour(request.getColour());

        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(savedVehicle);
    }

    // Get vehicle by ID
    public VehicleResponse getVehicleById(Long id) {

        Vehicle vehicle = findVehicle(id);

        return mapToResponse(vehicle);
    }

    // Get all vehicles
    public List<VehicleResponse> getAllVehicles() {

        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get vehicles by driver ID
    public List<VehicleResponse> getVehiclesByDriverId(Long driverId) {

        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException(
                    "Driver not found with ID: " + driverId
            );
        }

        return vehicleRepository.findByDriverId(driverId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Update vehicle
    public VehicleResponse updateVehicle(
            Long id,
            VehicleRequest request) {

        Vehicle vehicle = findVehicle(id);

        if (!driverRepository.existsById(request.getDriverId())) {
            throw new DriverNotFoundException(
                    "Driver not found with ID: " + request.getDriverId()
            );
        }

        vehicle.setDriverId(request.getDriverId());
        vehicle.setRegistrationNumber(request.getRegistrationNumber());
        vehicle.setVehicleType(request.getVehicleType());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setColour(request.getColour());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        return mapToResponse(updatedVehicle);
    }

    // Delete vehicle
    public void deleteVehicle(Long id) {

        Vehicle vehicle = findVehicle(id);

        vehicleRepository.delete(vehicle);
    }

    // Find vehicle internally
    private Vehicle findVehicle(Long id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new VehicleNotFoundException(
                                "Vehicle not found with ID: " + id
                        ));
    }

    // Convert Vehicle -> VehicleResponse
    private VehicleResponse mapToResponse(Vehicle vehicle) {

        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getDriverId(),
                vehicle.getRegistrationNumber(),
                vehicle.getVehicleType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColour()
        );
    }
}