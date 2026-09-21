package com.ridelink.driver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long id;
    private Long driverId;
    private String registrationNumber;
    private String vehicleType;
    private String brand;
    private String model;
    private String colour;
}
