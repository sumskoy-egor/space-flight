package com.space.flight.flightweb.service.auth;

import com.space.flight.flightweb.model.auth.SignInRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;

    @Value("${url-api-token}")
    private String apiUrl;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String login(String email, String password) {

        SignInRequest request = new SignInRequest(email, password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String body = responseEntity.getBody();

        return body;
    }

}
