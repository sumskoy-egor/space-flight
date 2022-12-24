package com.space.flightserver;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"h2db", "debug"})
class FlightServerApplicationTests {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void contextLoads() {
        assertNotNull(rest);
    }

    @Test
    @Order(1)
    void testCreateAstronaut() {

    }

    @Test
    @Order(2)
    void testCreateExpedition() {

    }

    @Test
    @Order(3)
    void testStartExpedition() {

    }

    @Test
    @Order(4)
    void testGetExpedition() {

    }
}
