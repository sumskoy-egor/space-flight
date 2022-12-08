package com.space.flight.flightweb.controller;

import com.space.flight.flightweb.model.auth.SignInRequest;
import com.space.flight.flightweb.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String logIn(@ModelAttribute("credentials") SignInRequest request, Model model) {

        model.addAttribute("email", request.getLogin());
        model.addAttribute("password", request.getPassword());

        String[] response = service.login(request.getLogin(), request.getPassword());
        model.addAttribute("code", response[0]);
        model.addAttribute("body", response[1]);
        model.addAttribute("headers", response[2]);

        return "test_page";
    }

}
