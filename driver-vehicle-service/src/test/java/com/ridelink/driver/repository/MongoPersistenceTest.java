package com.ridelink.driver.repository;

import com.ridelink.driver.enums.AvailabilityStatus;
import com.ridelink.driver.model.Driver;
import com.ridelink.driver.model.Vehicle;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MongoPersistenceTest {
    private static final String DATABASE = "ridelink_driver_test_" + UUID.randomUUID().toString().replace("-", "");

    @DynamicPropertySource
    static void database(DynamicPropertyRegistry properties) {
        properties.add("spring.mongodb.database", () -> DATABASE);
    }

    @Autowired DriverRepository drivers;
    @Autowired VehicleRepository vehicles;
    @Autowired MongoTemplate mongo;

    @AfterAll
    void removeOnlyThisTestDatabase() {
        assertEquals(DATABASE, mongo.getDb().getName());
        mongo.getDb().drop();
    }

    @Test
    void generatesIdsAndPersistsRelationshipsAndEnums() {
        Driver driver = drivers.save(new Driver(null, "account-test", "Colombo",
                6.9271, 79.8612, AvailabilityStatus.AVAILABLE));
        assertNotNull(driver.getId());
        Vehicle vehicle = vehicles.save(new Vehicle(null, driver.getId(), "MONGO-ROUNDTRIP",
                "CAR", "Toyota", "Prius", "White"));

        assertNotNull(vehicle.getId());
        assertEquals(driver, drivers.findById(driver.getId()).orElseThrow());
        assertEquals(driver, drivers.findByAccountId("account-test").orElseThrow());
        assertTrue(drivers.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE).contains(driver));
        assertEquals(vehicle, vehicles.findByDriverId(driver.getId()).get(0));
        assertTrue(vehicles.existsByRegistrationNumber("MONGO-ROUNDTRIP"));

        vehicle.setColour("Blue");
        vehicles.save(vehicle);
        assertEquals("Blue", vehicles.findById(vehicle.getId()).orElseThrow().getColour());
        vehicles.delete(vehicle);
        assertFalse(vehicles.existsById(vehicle.getId()));
    }

    @Test
    void readsImportedStringIdsAndDriverReferences() {
        mongo.getCollection("drivers").insertOne(new Document("_id", "1")
                .append("accountId", "1").append("availabilityStatus", "AVAILABLE"));
        mongo.getCollection("vehicles").insertOne(new Document("_id", "1")
                .append("driverId", "1").append("registrationNumber", "MONGO-LEGACY"));

        assertEquals("1", drivers.findById("1").orElseThrow().getAccountId());
        assertEquals(AvailabilityStatus.AVAILABLE, drivers.findById("1").orElseThrow().getAvailabilityStatus());
        assertEquals("1", vehicles.findByDriverId("1").get(0).getId());
    }

    @Test
    void uniqueIndexRejectsDuplicateRegistrationOnInsertAndUpdate() {
        vehicles.save(new Vehicle(null, "test-driver", "MONGO-UNIQUE", "CAR", "Toyota", "Prius", "White"));
        assertThrows(DuplicateKeyException.class, () -> vehicles.save(
                new Vehicle(null, "test-driver", "MONGO-UNIQUE", "CAR", "Honda", "Fit", "Blue")));

        Vehicle second = vehicles.save(new Vehicle(null, "test-driver", "MONGO-OTHER",
                "CAR", "Honda", "Fit", "Blue"));
        second.setRegistrationNumber("MONGO-UNIQUE");
        assertThrows(DuplicateKeyException.class, () -> vehicles.save(second));
        assertEquals("MONGO-OTHER", vehicles.findById(second.getId()).orElseThrow().getRegistrationNumber());
    }
}
