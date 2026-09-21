package com.ridelink.driver.dto;

import com.ridelink.driver.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private String id;
    private String accountId;
    private String serviceArea;
    private Double latitude;
    private Double longitude;
    private AvailabilityStatus availabilityStatus;
}