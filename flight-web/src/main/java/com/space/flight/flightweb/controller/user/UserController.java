package com.space.flight.flightweb.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flight.flightweb.model.IdDTO;
import com.space.flight.flightweb.model.user.UserRequest;
import com.space.flight.flightweb.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    // TODO TASKS
    /*

    todo delete unnecessary words from endpoint path (api/v3/)
    todo delete refresh token
    todo add logout
    todo edit docker-compose
    todo add README file
    */

    @GetMapping
    public String index() {
        return "users/user_actions";
    }

    //region redirection

    @GetMapping("/postOperatorRedirect")
    public String postOperatorRedirect(@ModelAttribute("user") UserRequest user) {
        return "users/user_create_operator";
    }

    @GetMapping("/postRecruiterRedirect")
    public String postRecruiterRedirect(@ModelAttribute("user") UserRequest user) {
        return "users/user_create_recruiter";
    }

    @GetMapping("/deleteRedirect")
    public String deleteRedirect(@ModelAttribute("id") IdDTO id) {
        return "users/user_delete";
    }

    //endregion

    @GetMapping("/getMe")
    public String getMe(@CookieValue(value = "accessToken", defaultValue = "") String accessToken, Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(service.getMe(accessToken),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "users/user_response";
    }

    @PostMapping("/operator")
    public String createOperator(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                                 @ModelAttribute("user") UserRequest user,
                                 Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(service.createOperator(accessToken, user),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "users/user_response";
    }

    @PostMapping("/recruiter")
    public String createRecruiter(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                                  @ModelAttribute("user") UserRequest user,
                                  Model model) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(service.createRecruiter(accessToken, user),
                    new TypeReference<Map<String, Object>>() {
                    });
            model.addAllAttributes(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "users/user_response";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@CookieValue(value = "accessToken", defaultValue = "") String accessToken,
                             @ModelAttribute("id") IdDTO id,
                             Model model) {

        service.delete(accessToken, id.getIdLong());
        model.addAttribute("result", "Your request has been processed");
        return "another_result";
    }

}
