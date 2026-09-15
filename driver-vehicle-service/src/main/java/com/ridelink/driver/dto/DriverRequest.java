package com.ridelink.driver.dto;

import com.ridelink.driver.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    private Double latitude;

    private Double longitude;

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus availabilityStatus;
}