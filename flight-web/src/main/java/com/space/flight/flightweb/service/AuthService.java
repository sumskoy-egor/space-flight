package com.space.flight.flightweb.service;

import com.space.flight.flightweb.model.auth.SignInRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private RestTemplate restTemplate = new RestTemplate();

    private static final String apiUrl = "http://localhost:8080/api/v3/token";

    public String[] login(String email, String password) {

        SignInRequest request = new SignInRequest(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        System.out.println("\nresponse status code - " + httpStatus + "\n");

        String body = responseEntity.getBody();
        System.out.println("response body - " + body + "\n");

        HttpHeaders responseHeaders = responseEntity.getHeaders();
        System.out.println("response headers - " + responseHeaders + "\n");

        return new String[]{httpStatus.toString(), body, responseHeaders.toString()};
    }

}
