package com.ridelink.driver.dto;

import com.ridelink.driver.enums.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {

    private Long id;
    private Long accountId;
    private String serviceArea;
    private Double latitude;
    private Double longitude;
    private AvailabilityStatus availabilityStatus;
}