package com.space.flight.flightweb.service.user;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    private static final String apiUrl = "http://localhost:8080/api/v3/users";


}
