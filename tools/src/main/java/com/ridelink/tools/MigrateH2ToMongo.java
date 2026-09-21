package com.ridelink.tools;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** One-time, repeatable import. Requires the H2 and MongoDB driver jars on the classpath. */
public class MigrateH2ToMongo {
    public static void main(String[] args) throws Exception {
        if (args.length < 3 || args.length > 4
                || (args.length == 4 && !args[3].equals("--apply"))) {
            throw new IllegalArgumentException(
                    "Usage: MigrateH2ToMongo.java <H2 path without .mv.db> <Mongo URI> <database> [--apply]");
        }
        Path source = Path.of(args[0]).toAbsolutePath();
        if (!Files.isRegularFile(Path.of(source + ".mv.db"))) {
            throw new IllegalArgumentException("H2 database file does not exist: " + source);
        }
        boolean apply = args.length == 4;
        Map<String, List<Document>> records = new LinkedHashMap<>();
        String jdbc = "jdbc:h2:file:" + source.toString().replace('\\', '/')
                + ";IFEXISTS=TRUE;ACCESS_MODE_DATA=r";
        try (var connection = DriverManager.getConnection(jdbc, "sa", "")) {
            try (var statement = connection.createStatement();
                 var tables = statement.executeQuery(
                         "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'")) {
                while (tables.next()) {
                    String table = tables.getString(1);
                    if (!table.equals("DRIVERS") && !table.equals("VEHICLES")) {
                        throw new IllegalStateException("Unmapped source table: " + table);
                    }
                }
            }
            try (var statement = connection.createStatement();
                 var rows = statement.executeQuery("SELECT * FROM DRIVERS ORDER BY ID")) {
                List<Document> drivers = new ArrayList<>();
                while (rows.next()) {
                    drivers.add(new Document("_id", rows.getString("ID"))
                            .append("accountId", rows.getString("ACCOUNT_ID"))
                            .append("serviceArea", rows.getString("SERVICE_AREA"))
                            .append("latitude", rows.getObject("LATITUDE"))
                            .append("longitude", rows.getObject("LONGITUDE"))
                            .append("availabilityStatus", rows.getString("AVAILABILITY_STATUS")));
                }
                records.put("drivers", drivers);
            }
            try (var statement = connection.createStatement();
                 var rows = statement.executeQuery("SELECT * FROM VEHICLES ORDER BY ID")) {
                List<Document> vehicles = new ArrayList<>();
                while (rows.next()) {
                    vehicles.add(new Document("_id", rows.getString("ID"))
                            .append("driverId", rows.getString("DRIVER_ID"))
                            .append("registrationNumber", rows.getString("REGISTRATION_NUMBER"))
                            .append("vehicleType", rows.getString("VEHICLE_TYPE"))
                            .append("brand", rows.getString("BRAND"))
                            .append("model", rows.getString("MODEL"))
                            .append("colour", rows.getString("COLOUR")));
                }
                records.put("vehicles", vehicles);
            }
        }
        var settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(args[1]))
                .applyToClusterSettings(builder -> builder.serverSelectionTimeout(5, TimeUnit.SECONDS))
                .build();
        try (var client = MongoClients.create(settings)) {
            MongoDatabase database = client.getDatabase(args[2]);
            database.runCommand(new Document("ping", 1));
            // Check every conflict before writing any data; never overwrite existing documents.
            for (var entry : records.entrySet()) {
                for (Document record : entry.getValue()) {
                    Document existing = database.getCollection(entry.getKey())
                            .find(new Document("_id", record.get("_id"))).first();
                    if (existing != null && !matches(record, existing)) {
                        throw new IllegalStateException("Conflicting " + entry.getKey()
                                + " ID " + record.get("_id") + "; no records imported");
                    }
                }
            }
            var registrations = new java.util.HashSet<String>();
            for (Document vehicle : records.get("vehicles")) {
                if (!registrations.add(vehicle.getString("registrationNumber"))) {
                    throw new IllegalStateException("Duplicate registration in H2 source");
                }
                boolean sourceDriver = records.get("drivers").stream()
                        .anyMatch(driver -> driver.get("_id").equals(vehicle.get("driverId")));
                if (!sourceDriver && database.getCollection("drivers")
                        .find(new Document("_id", vehicle.get("driverId"))).first() == null) {
                    throw new IllegalStateException("Missing driver for vehicle " + vehicle.get("_id"));
                }
                Document duplicate = database.getCollection("vehicles")
                        .find(new Document("registrationNumber", vehicle.get("registrationNumber")))
                        .first();
                if (duplicate != null && !duplicate.get("_id").equals(vehicle.get("_id"))) {
                    throw new IllegalStateException("Registration already belongs to another MongoDB vehicle");
                }
            }
            if (apply) {
                Path backup = Path.of(source + ".before-mongodb.mv.db");
                if (!Files.exists(backup)) {
                    Files.copy(Path.of(source + ".mv.db"), backup);
                }
                database.getCollection("vehicles").createIndex(new Document("registrationNumber", 1),
                        new IndexOptions().name("registrationNumber_1").unique(true));
                database.getCollection("vehicles").createIndex(new Document("driverId", 1),
                        new IndexOptions().name("driverId_1"));
            }
            for (var entry : records.entrySet()) {
                int inserted = 0;
                var collection = database.getCollection(entry.getKey());
                for (Document record : entry.getValue()) {
                    if (apply && collection.find(new Document("_id", record.get("_id"))).first() == null) {
                        collection.insertOne(record);
                        inserted++;
                    }
                    if (apply && !matches(record,
                            collection.find(new Document("_id", record.get("_id"))).first())) {
                        throw new IllegalStateException("Verification failed: " + entry.getKey());
                    }
                }
                System.out.printf("%s: source=%d, inserted=%d, MongoDB total=%d, %s%n",
                        entry.getKey(), entry.getValue().size(), inserted, collection.countDocuments(),
                        apply ? "all source fields verified" : "dry run (no writes)");
            }
        }
    }

    private static boolean matches(Document source, Document target) {
        return target != null && source.entrySet().stream()
                .allMatch(field -> java.util.Objects.equals(field.getValue(), target.get(field.getKey())));
    }
}
