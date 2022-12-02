package com.space.flightserver.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flightserver.config.security.properties.FlightSecurityProperties;
import com.space.flightserver.model.entity.user.request.SaveUserRequest;
import com.space.flightserver.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(FlightSecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final FlightSecurityProperties securityProperties;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

    public SecurityConfig(FlightSecurityProperties securityProperties,
                          UserService userService,
                          PasswordEncoder passwordEncoder,
                          ObjectMapper objectMapper) {
        this.securityProperties = securityProperties;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        setupDefaultAdmins();
    }

    private void setupDefaultAdmins() {
        List<SaveUserRequest> requests = securityProperties.getAdmins().entrySet().stream()
                .map(entry -> new SaveUserRequest(
                        entry.getValue().getEmail(),
                        new String(entry.getValue().getPassword()),
                        entry.getKey()))
                .peek(admin -> log.info("Default admin found: {} <{}>", admin.name(), admin.email()))
                .collect(Collectors.toList());
        userService.mergeAdmins(requests);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
    }
}
