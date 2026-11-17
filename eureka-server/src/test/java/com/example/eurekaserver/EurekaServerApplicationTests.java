package com.example.eurekaserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
        "eureka.server.enable-self-preservation=false"
    }
)
class EurekaServerApplicationTests {

    @Test
    void contextLoads() {
    }
}
