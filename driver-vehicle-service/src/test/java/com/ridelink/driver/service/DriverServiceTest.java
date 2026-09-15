package com.ridelink.driver.service;

import com.ridelink.driver.dto.AvailabilityRequest;
import com.ridelink.driver.dto.DriverRequest;
import com.ridelink.driver.dto.DriverResponse;
import com.ridelink.driver.dto.LocationRequest;
import com.ridelink.driver.enums.AvailabilityStatus;
import com.ridelink.driver.exception.DriverNotFoundException;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.repository.DriverRepository;
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
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;


    // Test 1 - Create driver successfully
    @Test
    void createDriver_success() {

        DriverRequest request = new DriverRequest();
        request.setAccountId(1L);
        request.setServiceArea("Colombo");
        request.setLatitude(6.9271);
        request.setLongitude(79.8612);
        request.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        when(driverRepository.save(any(Driver.class)))
                .thenAnswer(invocation -> {

                    Driver driver = invocation.getArgument(0);
                    driver.setId(1L);

                    return driver;
                });

        DriverResponse response =
                driverService.createDriver(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getAccountId());
        assertEquals("Colombo", response.getServiceArea());
        assertEquals(
                AvailabilityStatus.AVAILABLE,
                response.getAvailabilityStatus()
        );

        verify(driverRepository, times(1))
                .save(any(Driver.class));
    }


    // Test 2 - Get driver successfully
    @Test
    void getDriverById_success() {

        Driver driver = new Driver(
                1L,
                1L,
                "Colombo",
                6.9271,
                79.8612,
                AvailabilityStatus.AVAILABLE
        );

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        DriverResponse response =
                driverService.getDriverById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Colombo", response.getServiceArea());
    }


    // Test 3 - Driver not found
    @Test
    void getDriverById_notFound() {

        when(driverRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                DriverNotFoundException.class,
                () -> driverService.getDriverById(999L)
        );
    }


    // Test 4 - Update availability
    @Test
    void updateAvailability_success() {

        Driver driver = new Driver(
                1L,
                1L,
                "Colombo",
                6.9271,
                79.8612,
                AvailabilityStatus.UNAVAILABLE
        );

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(any(Driver.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        AvailabilityRequest request =
                new AvailabilityRequest();

        request.setStatus(
                AvailabilityStatus.AVAILABLE
        );

        DriverResponse response =
                driverService.updateAvailability(
                        1L,
                        request
                );

        assertEquals(
                AvailabilityStatus.AVAILABLE,
                response.getAvailabilityStatus()
        );
    }


    // Test 5 - Update location
    @Test
    void updateLocation_success() {

        Driver driver = new Driver(
                1L,
                1L,
                "Colombo",
                6.9271,
                79.8612,
                AvailabilityStatus.AVAILABLE
        );

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        when(driverRepository.save(any(Driver.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        LocationRequest request =
                new LocationRequest();

        request.setLatitude(7.2083);
        request.setLongitude(79.8358);

        DriverResponse response =
                driverService.updateLocation(
                        1L,
                        request
                );

        assertEquals(
                7.2083,
                response.getLatitude()
        );

        assertEquals(
                79.8358,
                response.getLongitude()
        );
    }


    // Test 6 - Get available drivers
    @Test
    void getAvailableDrivers_success() {

        Driver driver1 = new Driver(
                1L,
                1L,
                "Colombo",
                6.9271,
                79.8612,
                AvailabilityStatus.AVAILABLE
        );

        Driver driver2 = new Driver(
                2L,
                2L,
                "Negombo",
                7.2083,
                79.8358,
                AvailabilityStatus.AVAILABLE
        );

        when(
                driverRepository.findByAvailabilityStatus(
                        AvailabilityStatus.AVAILABLE
                )
        ).thenReturn(
                List.of(driver1, driver2)
        );

        List<DriverResponse> drivers =
                driverService.getAvailableDrivers();

        assertEquals(2, drivers.size());

        assertEquals(
                AvailabilityStatus.AVAILABLE,
                drivers.get(0)
                        .getAvailabilityStatus()
        );
    }
}