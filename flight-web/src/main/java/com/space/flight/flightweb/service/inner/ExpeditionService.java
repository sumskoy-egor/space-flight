package com.space.flight.flightweb.service.inner;

import com.space.flight.flightweb.model.inner.ExpeditionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExpeditionService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${url-api-expeditions}")
    private String apiUrl;

    @SuppressWarnings("Duplicates")
    public String get(String accessToken, Long id) {

        HttpHeaders headers = getHttpHeaders(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl + "/{id}",
                HttpMethod.GET,
                requestEntity,
                String.class,
                id.toString());

        return responseEntity.getBody();
    }

    public String create(String accessToken, ExpeditionRequest request) {

        HttpHeaders headers = getHttpHeaders(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        return responseEntity.getBody();
    }

    @SuppressWarnings("Duplicates")
    public String delete(String accessToken, Long id) {

        HttpHeaders headers = getHttpHeaders(accessToken);

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

    public String start(String accessToken, Long id) {

        HttpHeaders headers = getHttpHeaders(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(apiUrl + "/start/{id}",
                    HttpMethod.PUT,
                    requestEntity,
                    String.class,
                    id.toString());
            return "success";
        } catch (Exception e) {
            return null;
        }
    }

    public String complete(String accessToken, Long id) {

        HttpHeaders headers = getHttpHeaders(accessToken);

        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange(apiUrl + "/complete/{id}",
                    HttpMethod.PUT,
                    requestEntity,
                    String.class,
                    id.toString());
            return "success";
        } catch (Exception e) {
            return null;
        }
    }

    private HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, ("Bearer " + accessToken));
        return headers;
    }
}
