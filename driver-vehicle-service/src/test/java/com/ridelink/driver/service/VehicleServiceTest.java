package com.ridelink.driver.service;

import com.ridelink.driver.dto.VehicleRequest;
import com.ridelink.driver.dto.VehicleResponse;
import com.ridelink.driver.exception.DriverNotFoundException;
import com.ridelink.driver.exception.VehicleNotFoundException;
import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.repository.DriverRepository;
import com.ridelink.driver.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private VehicleService vehicleService;


    // Test 1 - Add vehicle successfully
    @Test
    void addVehicle_success() {

        VehicleRequest request =
                new VehicleRequest();

        request.setDriverId("1");
        request.setRegistrationNumber("CAB-1234");
        request.setVehicleType("CAR");
        request.setBrand("Toyota");
        request.setModel("Prius");
        request.setColour("White");

        when(driverRepository.existsById("1"))
                .thenReturn(true);

        when(
                vehicleRepository
                        .existsByRegistrationNumber(
                                "CAB-1234"
                        )
        ).thenReturn(false);

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenAnswer(invocation -> {

                    Vehicle vehicle =
                            invocation.getArgument(0);

                    vehicle.setId("1");

                    return vehicle;
                });

        VehicleResponse response =
                vehicleService.addVehicle(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals(
                "CAB-1234",
                response.getRegistrationNumber()
        );
        assertEquals(
                "Toyota",
                response.getBrand()
        );
    }


    // Test 2 - Driver does not exist
    @Test
    void addVehicle_driverNotFound() {

        VehicleRequest request =
                new VehicleRequest();

        request.setDriverId("999");
        request.setRegistrationNumber("CAB-1234");
        request.setVehicleType("CAR");
        request.setBrand("Toyota");
        request.setModel("Prius");
        request.setColour("White");

        when(driverRepository.existsById("999"))
                .thenReturn(false);

        assertThrows(
                DriverNotFoundException.class,
                () -> vehicleService.addVehicle(request)
        );

        verify(
                vehicleRepository,
                never()
        ).save(any(Vehicle.class));
    }


    // Test 3 - Duplicate registration number
    @Test
    void addVehicle_duplicateRegistration() {

        VehicleRequest request =
                new VehicleRequest();

        request.setDriverId("1");
        request.setRegistrationNumber("CAB-1234");
        request.setVehicleType("CAR");
        request.setBrand("Toyota");
        request.setModel("Prius");
        request.setColour("White");

        when(driverRepository.existsById("1"))
                .thenReturn(true);

        when(
                vehicleRepository
                        .existsByRegistrationNumber(
                                "CAB-1234"
                        )
        ).thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> vehicleService.addVehicle(request)
        );
    }


    // Test 4 - Get vehicle successfully
    @Test
    void getVehicleById_success() {

        Vehicle vehicle = new Vehicle(
                "1",
                "1",
                "CAB-1234",
                "CAR",
                "Toyota",
                "Prius",
                "White"
        );

        when(vehicleRepository.findById("1"))
                .thenReturn(Optional.of(vehicle));

        VehicleResponse response =
                vehicleService.getVehicleById("1");

        assertEquals("1", response.getId());

        assertEquals(
                "CAB-1234",
                response.getRegistrationNumber()
        );
    }


    // Test 5 - Vehicle not found
    @Test
    void getVehicleById_notFound() {

        when(vehicleRepository.findById("999"))
                .thenReturn(Optional.empty());

        assertThrows(
                VehicleNotFoundException.class,
                () -> vehicleService
                        .getVehicleById("999")
        );
    }


    // Test 6 - Get vehicles by driver
    @Test
    void getVehiclesByDriverId_success() {

        Vehicle vehicle = new Vehicle(
                "1",
                "1",
                "CAB-1234",
                "CAR",
                "Toyota",
                "Prius",
                "White"
        );

        when(driverRepository.existsById("1"))
                .thenReturn(true);

        when(vehicleRepository.findByDriverId("1"))
                .thenReturn(List.of(vehicle));

        List<VehicleResponse> vehicles =
                vehicleService
                        .getVehiclesByDriverId("1");

        assertEquals(1, vehicles.size());

        assertEquals(
                "Toyota",
                vehicles.get(0).getBrand()
        );
    }


    // Test 7 - Delete vehicle
    @Test
    void deleteVehicle_success() {

        Vehicle vehicle = new Vehicle(
                "1",
                "1",
                "CAB-1234",
                "CAR",
                "Toyota",
                "Prius",
                "White"
        );

        when(vehicleRepository.findById("1"))
                .thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle("1");

        verify(
                vehicleRepository,
                times(1)
        ).delete(vehicle);
    }
}