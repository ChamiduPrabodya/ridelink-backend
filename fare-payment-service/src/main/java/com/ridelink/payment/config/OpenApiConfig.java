package com.ridelink.payment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rideLinkOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("RideLink Fare & Payment Service API")
                        .version("1.0")
                        .description(
                                "REST API for fare estimation, final fare calculation, "
                                        + "simulated payment recording, payment status retrieval, "
                                        + "and receipt retrieval."));
    }
}
