package com.ridelink.driver.model;

import com.ridelink.driver.enums.AvailabilityStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    private String id;

    private String accountId;

    private String serviceArea;

    private Double latitude;

    private Double longitude;

    private AvailabilityStatus availabilityStatus;
}
