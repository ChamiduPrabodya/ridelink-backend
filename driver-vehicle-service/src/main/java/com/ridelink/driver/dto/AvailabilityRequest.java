package com.ridelink.driver.dto;

import com.ridelink.driver.enums.AvailabilityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvailabilityRequest {

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus status;
}