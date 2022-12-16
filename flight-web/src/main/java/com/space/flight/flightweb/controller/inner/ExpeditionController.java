package com.space.flight.flightweb.controller.inner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flight.flightweb.model.IdDTO;
import com.space.flight.flightweb.model.inner.ExpeditionRequest;
import com.space.flight.flightweb.service.inner.ExpeditionService;
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
@RequestMapping("/expeditions")
public class ExpeditionController {

    private final ExpeditionService service;

    public ExpeditionController(ExpeditionService service) {
        this.service = service;
    }

    @GetMapping
    public String index() {
        return "expeditions/expeditions_actions";
    }

    //region redirection

    @GetMapping("/getByIdRedirect")
    public String getRedirect(@ModelAttribute("id") IdDTO id) {
        return "expeditions/expedition_get";
    }

    @GetMapping("/postRedirect")
    public String postRedirect(@ModelAttribute("expedition") ExpeditionRequest expedition) {
        return "expeditions/expedition_post";
    }

    @GetMapping("/deleteRedirect")
    public String deleteRedirect(@ModelAttribute("id") IdDTO id) {
        return "expeditions/expedition_delete";
    }

    @GetMapping("/operationsRedirect")
    public String operationsRedirect(@ModelAttribute("id") IdDTO id) {
        return "expeditions/expedition_operate";
    }

    //endregion

    @GetMapping("/getById")
    public String getById(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                          @ModelAttribute("id") IdDTO id,
                          Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(service.get(accessToken, id.getIdLong()),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "expeditions/expedition_response";
    }

    @PostMapping
    public String post(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                       @ModelAttribute("expedition") ExpeditionRequest expedition,
                       Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            expedition.parseAstronauts();
            expedition.setDateFormat();

            Map<String, Object> map = objectMapper.readValue(service.create(accessToken, expedition),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "expeditions/expedition_response";
    }

    @DeleteMapping("/delete")
    public String delete(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                         @ModelAttribute("id") IdDTO id,
                         Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String response = service.delete(accessToken, id.getIdLong());

            if (response == null) {
                model.addAttribute("result", "Expedition does not exist");
                return "another_result";
            }

            Map<String, Object> map = objectMapper.readValue((response),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
            return "expeditions/expedition_response";

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/start")
    public String start(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                        @ModelAttribute("id") IdDTO id,
                        Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        String response = service.start(accessToken, id.getIdLong());
        if (response == null) {
            model.addAttribute("result", "Something went wrong");
            return "another_result";
        } else {
            model.addAttribute("result",
                    "Expedition " + id.getIdLong() + " has been started successfully");
            return "another_result";
        }
    }

    @PutMapping("/complete")
    public String complete(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                           @ModelAttribute("id") IdDTO id,
                           Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        String response = service.complete(accessToken, id.getIdLong());
        if (response == null) {
            model.addAttribute("result", "Something went wrong");
        } else {
            model.addAttribute("result",
                    "Expedition " + id.getIdLong() + " has been completed successfully");
        }
        return "another_result";
    }
}
