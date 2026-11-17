package com.example.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Driver Microservice
 * Registers with Eureka and serves driver resources.
 * Can be run concurrently on port 8081 and port 8082.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }
}
