package com.example.driverservice;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false"
    }
)
class DriverServiceApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("=== TEST CONTEXT LOADED SUCCESSFULLY ===");
    }

    @AfterAll
    static void checkThreads() {
        System.out.println("=== DUMPING NON-DAEMON THREADS ===");
        Thread.getAllStackTraces().keySet().forEach(t -> {
            if (!t.isDaemon() && t.isAlive()) {
                System.out.println("Non-daemon thread: " + t.getName() + " (" + t.getState() + ")");
            }
        });
    }
}
