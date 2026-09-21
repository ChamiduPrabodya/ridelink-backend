package com.ridelink.driver.repository;

import com.mongodb.client.MongoClients;
import com.mongodb.client.model.IndexOptions;
import com.ridelink.driver.DriverVehicleServiceApplication;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MongoIndexStartupTest {
    @Test
    void startsWithIndexesAlreadyCreatedByMigration() {
        String uri = System.getenv().getOrDefault("MONGODB_URI", "mongodb://localhost:27017/");
        String databaseName = "ridelink_index_test_" + UUID.randomUUID().toString().replace("-", "");

        try (var client = MongoClients.create(uri)) {
            var database = client.getDatabase(databaseName);
            try {
                var vehicles = database.getCollection("vehicles");
                // Match the indexes created by MongoDB's driver during the original import.
                vehicles.createIndex(new Document("driverId", 1));
                vehicles.createIndex(new Document("registrationNumber", 1), new IndexOptions().unique(true));

                try (var application = SpringApplication.run(DriverVehicleServiceApplication.class,
                        "--server.port=0", "--spring.mongodb.uri=" + uri,
                        "--spring.mongodb.database=" + databaseName,
                        "--spring.data.mongodb.auto-index-creation=true")) {
                    var mongo = application.getBean(MongoTemplate.class);
                    assertEquals(databaseName, mongo.getDb().getName());
                    assertEquals(3, vehicles.listIndexes().into(new ArrayList<>()).size());
                }
            } finally {
                database.drop();
            }
        }
    }
}
