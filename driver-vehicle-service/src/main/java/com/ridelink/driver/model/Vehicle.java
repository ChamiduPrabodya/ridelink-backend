package com.ridelink.driver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    private String id;

    @Indexed(name = "driverId_1")
    private String driverId;

    @Indexed(name = "registrationNumber_1", unique = true)
    private String registrationNumber;

    private String vehicleType;

    private String brand;

    private String model;

    private String colour;
}
