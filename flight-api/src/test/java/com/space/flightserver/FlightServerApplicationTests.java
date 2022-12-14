package com.space.flightserver;

import com.space.flightserver.model.entity.inner.dto.AstronautResponse;
import com.space.flightserver.model.entity.inner.request.CreateAstronautRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    @WithMockUser(username = "test", roles = "OPERATOR")
    void testCreateAstronaut() {
        String expectedName = "test";
        Boolean expectedState = true;
        ResponseEntity<AstronautResponse> astronautResponseEntity = createAstronaut(expectedName, expectedState);

        assertEquals(HttpStatus.CREATED, astronautResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, astronautResponseEntity.getHeaders().getContentType());

        AstronautResponse response = astronautResponseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(expectedName, response.name());
        assertEquals(expectedState, response.isBusy());
    }

    private ResponseEntity<AstronautResponse> createAstronaut(String name, Boolean busy) {
        String url = Routes.ASTRONAUTS;
        CreateAstronautRequest request = new CreateAstronautRequest(name, busy);

        return rest.postForEntity(url, request, AstronautResponse.class);
    }
}
