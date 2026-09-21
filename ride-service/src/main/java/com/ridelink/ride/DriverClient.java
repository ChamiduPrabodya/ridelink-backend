package com.ridelink.ride;

import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DriverClient {

    /**
     * Returns the id of an available driver, or empty if none is free.
     *
     * TEMPORARY STUB: returns a fixed driver id so ride assignment works
     * before the Driver & Vehicle Service is merged. Once it is available,
     * replace the body with a real HTTP call to that service's
     * GET /drivers/available endpoint (e.g. using RestClient or WebClient).
     */
    public Optional<Long> findAvailableDriver() {
        return Optional.of(1L);
    }
}
