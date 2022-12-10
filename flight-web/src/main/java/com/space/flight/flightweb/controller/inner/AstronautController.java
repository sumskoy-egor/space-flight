package com.space.flight.flightweb.controller.inner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flight.flightweb.model.inner.AstronautRequest;
import com.space.flight.flightweb.service.inner.AstronautService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/astronauts")
public class AstronautController {

    private final AstronautService service;

    private String accessToken;

    public AstronautController(AstronautService service) {
        this.service = service;
    }

    @GetMapping
    public String index(@CookieValue(value = "accessToken", defaultValue = "") String accessToken) {
        this.accessToken = accessToken;

        return "astronauts/astronauts_actions";
    }

    //region redirection

    @GetMapping("/getByIdRedirect")
    public String getByIdRedirect(@ModelAttribute("astronaut") AstronautRequest astronaut) {
        return "astronauts/astronauts_get_by_id";
    }

    @GetMapping("/postRedirect")
    public String postRedirect(@ModelAttribute("astronaut") AstronautRequest astronaut) {
        return "astronauts/astronaut_post";
    }

    @GetMapping("/deleteRedirect")
    public String deleteRedirect(@ModelAttribute("astronaut") AstronautRequest astronautRequest) {
        return "astronauts/astronaut_delete";
    }

    @GetMapping("/putRedirect")
    public String putRedirect(@ModelAttribute("astronaut") AstronautRequest astronautRequest) {
        return "astronauts/astronaut_put";
    }

    //endregion

    @GetMapping("/getById")
    public String getByID(@ModelAttribute("astronaut") AstronautRequest astronaut, Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.setAccessToken(accessToken);
            Map<String, String> map = objectMapper.readValue(service.getById(astronaut.getId()),
                    new TypeReference<Map<String, String>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "astronauts/astronaut_response";

    }

    @PostMapping
    public String post(@ModelAttribute("astronaut") AstronautRequest astronaut, Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.setAccessToken(accessToken);
            Map<String, String> map = objectMapper.readValue(service.post(astronaut),
                    new TypeReference<Map<String, String>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "astronauts/astronaut_response";
    }

    @DeleteMapping("/delete")
    public String deleteById(@ModelAttribute("astronaut") AstronautRequest astronaut, Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            service.setAccessToken(accessToken);
            String response = service.delete(astronaut.getId());
            return astronautResponsePage(model, objectMapper, response);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/put")
    public String put(@ModelAttribute("astronaut") AstronautRequest astronaut, Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        service.setAccessToken(accessToken);
        String response = service.put(astronaut);
        if (response == null) {
            model.addAttribute("result", "Astronaut does not exist");
            return "astronauts/astronaut_another_result";
        } else {
            model.addAttribute("result", "Astronaut has been put");
            return "astronauts/astronaut_another_result";
        }

    }

    private String astronautResponsePage(Model model,
                                         ObjectMapper objectMapper,
                                         String response) throws JsonProcessingException {
        if (response == null) {
            model.addAttribute("result", "Astronaut does not exist");
            return "astronauts/astronaut_another_result";
        }

        Map<String, String> map = objectMapper.readValue((response),
                new TypeReference<Map<String, String>>() {
                });
        model.addAllAttributes(map);
        return "astronauts/astronaut_response";
    }

}
