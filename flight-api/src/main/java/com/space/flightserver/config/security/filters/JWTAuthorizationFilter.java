package com.space.flightserver.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.space.flightserver.config.security.SecurityConstants;
import com.space.flightserver.config.security.properties.FlightJWTProperties;
import com.space.flightserver.model.entity.user.KnownAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    private final Algorithm algorithm;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, FlightJWTProperties jwtProperties) {
        super(authenticationManager);
        this.algorithm = Algorithm.HMAC512(new String(jwtProperties.getSecret()).getBytes());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        var securityContext = SecurityContextHolder.getContext();

        var authentication = securityContext.getAuthentication();
        // if authenticated by other means, such as JWTAuthenticationFilter
        if (authentication != null && authentication.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(SecurityConstants.AUTH_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String encodedJwt = header.substring(SecurityConstants.AUTH_TOKEN_PREFIX.length());
        authentication = getAuthentication(encodedJwt);

        securityContext.setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String encodedJwt) {
        // parse the token.
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(encodedJwt);
        } catch (Exception e) {
            log.debug("Invalid JWT received", e);
            return null;
        }

        String email = decodedJWT.getSubject();

        Set<KnownAuthority> authorities = decodedJWT.getClaim(SecurityConstants.AUTHORITIES_CLAIM)
                .asList(String.class).stream()
                .map(KnownAuthority::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(KnownAuthority.class)));

        return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
}
