package com.example.driverservice.controller;

import com.example.driverservice.model.Driver;
import com.example.driverservice.model.DriverResponse;
import com.example.driverservice.model.ServiceInstanceInfo;
import com.example.driverservice.service.DriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * REST Controller exposing Driver management endpoints.
 * Emits serving port and instance ID in headers and response body
 * to verify Round-Robin Load Balancing behavior.
 */
@RestController
@RequestMapping("/drivers")
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);

    private final DriverService driverService;

    @Value("${server.port:8081}")
    private int serverPort;

    @Value("${spring.application.name:driver-service}")
    private String applicationName;

    @Value("${eureka.instance.instance-id:${spring.application.name:driver-service}:${server.port:8081}}")
    private String instanceId;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    private HttpHeaders buildInstanceHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Handled-By-Service", applicationName);
        headers.add("X-Handled-By-Port", String.valueOf(serverPort));
        headers.add("X-Handled-By-Instance", instanceId);
        return headers;
    }

    private String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    @GetMapping
    public ResponseEntity<DriverResponse<List<Driver>>> getAllDrivers() {
        log.info(">>> [Port: {}] Received request: GET /drivers", serverPort);
        List<Driver> drivers = driverService.getAllDrivers();
        DriverResponse<List<Driver>> response = new DriverResponse<>(
                applicationName,
                serverPort,
                instanceId,
                "Drivers fetched successfully from instance running on port " + serverPort,
                drivers
        );
        return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse<Driver>> getDriverById(@PathVariable Long id) {
        log.info(">>> [Port: {}] Received request: GET /drivers/{}", serverPort, id);
        return driverService.getDriverById(id)
                .map(driver -> {
                    DriverResponse<Driver> response = new DriverResponse<>(
                            applicationName,
                            serverPort,
                            instanceId,
                            "Driver found by instance on port " + serverPort,
                            driver
                    );
                    return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.OK);
                })
                .orElseGet(() -> {
                    DriverResponse<Driver> response = new DriverResponse<>(
                            applicationName,
                            serverPort,
                            instanceId,
                            "Driver not found with id: " + id,
                            null
                    );
                    return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.NOT_FOUND);
                });
    }

    @GetMapping("/info")
    public ResponseEntity<ServiceInstanceInfo> getInstanceInfo() {
        log.info(">>> [Port: {}] Received request: GET /drivers/info", serverPort);
        ServiceInstanceInfo info = new ServiceInstanceInfo(
                applicationName,
                serverPort,
                instanceId,
                getHostName(),
                "UP",
                "Hello from Driver Service instance active on port " + serverPort
        );
        return new ResponseEntity<>(info, buildInstanceHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverResponse<Driver>> createDriver(@RequestBody Driver driver) {
        log.info(">>> [Port: {}] Received request: POST /drivers - Name: {}", serverPort, driver.getName());
        Driver created = driverService.createDriver(driver);
        DriverResponse<Driver> response = new DriverResponse<>(
                applicationName,
                serverPort,
                instanceId,
                "Driver created successfully on instance with port " + serverPort,
                created
        );
        return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse<Driver>> updateDriver(@PathVariable Long id, @RequestBody Driver driver) {
        log.info(">>> [Port: {}] Received request: PUT /drivers/{}", serverPort, id);
        return driverService.updateDriver(id, driver)
                .map(updated -> {
                    DriverResponse<Driver> response = new DriverResponse<>(
                            applicationName,
                            serverPort,
                            instanceId,
                            "Driver updated on instance with port " + serverPort,
                            updated
                    );
                    return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DriverResponse<Void>> deleteDriver(@PathVariable Long id) {
        log.info(">>> [Port: {}] Received request: DELETE /drivers/{}", serverPort, id);
        boolean deleted = driverService.deleteDriver(id);
        if (deleted) {
            DriverResponse<Void> response = new DriverResponse<>(
                    applicationName,
                    serverPort,
                    instanceId,
                    "Driver " + id + " deleted on instance with port " + serverPort,
                    null
            );
            return new ResponseEntity<>(response, buildInstanceHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
