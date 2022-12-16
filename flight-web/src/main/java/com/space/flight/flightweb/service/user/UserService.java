package com.space.flight.flightweb.service.user;

import com.space.flight.flightweb.model.user.UserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${url-api-users}")
    private String apiUrl;

    public String getMe(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/me",
                HttpMethod.GET,
                requestEntity,
                String.class);

        return responseEntity.getBody();
    }

    public String createOperator(String accessToken, UserRequest request) {
        return createUser(accessToken, "operator", request);
    }

    public String createRecruiter(String accessToken, UserRequest request) {
        return createUser(accessToken, "recruiter", request);
    }

    private String createUser(String accessToken, String role, UserRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/" + role,
                HttpMethod.POST,
                requestEntity,
                String.class);

        return responseEntity.getBody();
    }

    @SuppressWarnings("Duplicates")
    public void delete(String accessToken, Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(apiUrl + "/{id}",
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class,
                    id.toString());
        } catch (HttpClientErrorException ignored) {

        }
    }
}
