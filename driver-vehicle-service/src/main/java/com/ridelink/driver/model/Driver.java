package com.ridelink.driver.model;

import com.ridelink.driver.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private String serviceArea;

    private Double latitude;

    private Double longitude;

    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;
}