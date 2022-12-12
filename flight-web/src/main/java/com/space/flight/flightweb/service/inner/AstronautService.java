package com.space.flight.flightweb.service.inner;

import com.space.flight.flightweb.model.inner.AstronautRequest;
import com.space.flight.flightweb.service.ServiceTokenAccessible;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AstronautService implements ServiceTokenAccessible {

    private final RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    private static final String apiUrl = "http://localhost:8080/api/v3/astronauts";

    public String getById(Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/{id}",
                HttpMethod.GET,
                requestEntity,
                String.class,
                id.toString());

        return responseEntity.getBody();
    }

    public String post(AstronautRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        return responseEntity.getBody();
    }

    @SuppressWarnings("Duplicates")
    public String delete(Long id) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/{id}",
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class,
                    id.toString());
            return responseEntity.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String put(AstronautRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        try {
            restTemplate.exchange(apiUrl + "/{id}",
                    HttpMethod.PUT,
                    requestEntity,
                    String.class,
                    request.getId().toString());
            return "success";
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
