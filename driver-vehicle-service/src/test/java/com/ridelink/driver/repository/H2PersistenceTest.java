package com.ridelink.driver.repository;

import com.ridelink.driver.enums.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:driver_persistence_test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class H2PersistenceTest {
    @Autowired DriverRepository drivers;
    @Autowired VehicleRepository vehicles;

    @Test
    void generatesNumericIdsAndPersistsDriverVehicleRelationship() {
        Driver driver = drivers.save(new Driver(null, 101L, "Colombo", 6.9271, 79.8612,
                AvailabilityStatus.AVAILABLE));
        assertNotNull(driver.getId());
        assertTrue(driver.getId() > 0);
        assertEquals(driver, drivers.findByAccountId(101L).orElseThrow());
        assertTrue(drivers.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE).contains(driver));

        Vehicle vehicle = vehicles.save(new Vehicle(null, driver.getId(), "H2-ROUNDTRIP",
                "CAR", "Toyota", "Prius", "White"));
        assertNotNull(vehicle.getId());
        assertEquals(vehicle, vehicles.findByDriverId(driver.getId()).get(0));
        vehicle.setColour("Blue");
        vehicles.save(vehicle);
        assertEquals("Blue", vehicles.findById(vehicle.getId()).orElseThrow().getColour());
        vehicles.delete(vehicle);
        assertFalse(vehicles.existsById(vehicle.getId()));
    }

    @Test
    void rejectsDuplicateRegistrationOnInsertAndUpdate() {
        Driver driver = drivers.save(new Driver(null, 102L, "Colombo", null, null,
                AvailabilityStatus.AVAILABLE));
        vehicles.saveAndFlush(new Vehicle(null, driver.getId(), "H2-UNIQUE",
                "CAR", "Toyota", "Prius", "White"));
        assertThrows(DataIntegrityViolationException.class, () -> vehicles.saveAndFlush(
                new Vehicle(null, driver.getId(), "H2-UNIQUE", "CAR", "Honda", "Fit", "Blue")));
        Vehicle second = vehicles.saveAndFlush(new Vehicle(null, driver.getId(), "H2-OTHER",
                "CAR", "Honda", "Fit", "Blue"));
        second.setRegistrationNumber("H2-UNIQUE");
        assertThrows(DataIntegrityViolationException.class, () -> vehicles.saveAndFlush(second));
        assertEquals("H2-OTHER", vehicles.findById(second.getId()).orElseThrow().getRegistrationNumber());
    }
}
