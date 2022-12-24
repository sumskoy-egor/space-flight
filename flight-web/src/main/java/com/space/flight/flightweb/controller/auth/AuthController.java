package com.space.flight.flightweb.controller.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flight.flightweb.model.auth.SignInRequest;
import com.space.flight.flightweb.service.auth.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping
    public String redirect(@ModelAttribute("credentials") SignInRequest request) {
        return "token_auth";
    }

    @PostMapping
    public String logIn(HttpServletResponse servletResponse, @ModelAttribute("credentials") SignInRequest request) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, String> map = objectMapper.readValue(service.login(request.getLogin(), request.getPassword()),
                    new TypeReference<Map<String, String>>() {});

            Cookie cookie = new Cookie("accessToken", map.get("accessToken"));
            cookie.setMaxAge(600);
            cookie.setPath("/");
            servletResponse.addCookie(cookie);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return "action_page";
    }

    @PostMapping("/logout")
    public String logOut(HttpServletResponse servletResponse, @ModelAttribute("credentials") SignInRequest request) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);

        return "token_auth";
    }
}
