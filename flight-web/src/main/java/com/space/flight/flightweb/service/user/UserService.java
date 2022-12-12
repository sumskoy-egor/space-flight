package com.space.flight.flightweb.service.user;

import com.space.flight.flightweb.model.user.UserRequest;
import com.space.flight.flightweb.service.ServiceTokenAccessible;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService implements ServiceTokenAccessible {

    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    private static final String apiUrl = "http://localhost:8080/api/v3/users";

    public String getMe() {

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

    public String createOperator(UserRequest request) {
        return createUser("operator", request);
    }

    public String createRecruiter(UserRequest request) {
        return createUser("recruiter", request);
    }

    private String createUser(String role, UserRequest request) {
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
    public void delete(Long id) {

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

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
