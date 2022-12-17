package com.space.flightserver.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.flightserver.Routes;
import com.space.flightserver.config.security.filters.JWTAuthenticationFilter;
import com.space.flightserver.config.security.filters.JWTAuthorizationFilter;
import com.space.flightserver.config.security.properties.FlightSecurityProperties;
import com.space.flightserver.model.entity.user.request.SaveUserRequest;
import com.space.flightserver.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                        entry.getKey(),
                        new String(entry.getValue().getPassword())
                ))
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
        http.authorizeRequests()
                // open static resources
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // open swagger-ui
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // allow user registration, ignore authorization filters on login
                .antMatchers(HttpMethod.POST, Routes.TOKEN).permitAll()
                // getting info about current account
                .antMatchers(HttpMethod.GET, Routes.USERS + "/me").authenticated()
                // recruiter can edit astronauts
                .antMatchers(Routes.ASTRONAUTS + "/**").hasRole("RECRUITER")
                // operator can edit spaceflights
                .antMatchers(Routes.EXPEDITIONS + "/**").hasRole("OPERATOR")
                // admin can manage users by id
                // admin can use Actuator endpoints
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
                // by default, require admin rights
                .anyRequest().hasRole("ADMIN")
                .and()
                // auth filter
                .addFilter(jwtAuthenticationFilter())
                // jwt-verification filter
                .addFilter(jwtAuthorizationFilter())
                // for unauthorized requests return 401
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                // allow cross-origin requests for all endpoints
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        var filter = new JWTAuthenticationFilter(authenticationManager(), objectMapper);
        filter.setFilterProcessesUrl(Routes.TOKEN);
        return filter;
    }

    private JWTAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JWTAuthorizationFilter(authenticationManager(), securityProperties.getJwtProperties());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
