package com.example.driverservice.service;

import com.example.driverservice.model.Driver;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service managing Driver data with thread-safe in-memory storage.
 */
@Service
public class DriverService {

    private final Map<Long, Driver> driverStore = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @PostConstruct
    public void initData() {
        createDriver(new Driver(null, "Alexander Wright", "DL-982143", "Sedan", "KA-01-AB-1234", 4.9, "AVAILABLE", "+1-555-0101"));
        createDriver(new Driver(null, "Sophia Martinez", "DL-472910", "SUV", "KA-02-CD-5678", 4.8, "AVAILABLE", "+1-555-0102"));
        createDriver(new Driver(null, "Marcus Chen", "DL-192837", "Hatchback", "KA-03-EF-9012", 4.7, "ON_TRIP", "+1-555-0103"));
        createDriver(new Driver(null, "Elena Rostova", "DL-837461", "Luxury Sedan", "KA-04-GH-3456", 5.0, "AVAILABLE", "+1-555-0104"));
        createDriver(new Driver(null, "David Kim", "DL-562918", "Electric", "KA-05-IJ-7890", 4.6, "OFFLINE", "+1-555-0105"));
    }

    public List<Driver> getAllDrivers() {
        return new ArrayList<>(driverStore.values());
    }

    public Optional<Driver> getDriverById(Long id) {
        return Optional.ofNullable(driverStore.get(id));
    }

    public Driver createDriver(Driver driver) {
        if (driver.getId() == null) {
            driver.setId(idSequence.incrementAndGet());
        } else {
            idSequence.updateAndGet(current -> Math.max(current, driver.getId()));
        }
        if (driver.getStatus() == null || driver.getStatus().isBlank()) {
            driver.setStatus("AVAILABLE");
        }
        if (driver.getRating() == null) {
            driver.setRating(5.0);
        }
        driverStore.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> updateDriver(Long id, Driver updatedDriver) {
        if (!driverStore.containsKey(id)) {
            return Optional.empty();
        }
        updatedDriver.setId(id);
        driverStore.put(id, updatedDriver);
        return Optional.of(updatedDriver);
    }

    public boolean deleteDriver(Long id) {
        return driverStore.remove(id) != null;
    }
}
