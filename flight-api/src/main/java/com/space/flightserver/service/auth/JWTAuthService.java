package com.space.flightserver.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.space.flightserver.config.security.SecurityConstants;
import com.space.flightserver.config.security.properties.FlightSecurityProperties;
import com.space.flightserver.model.entity.auth.FlightUserDetails;
import com.space.flightserver.model.entity.auth.response.AccessTokenResponse;
import com.space.flightserver.repository.UserRepository;
import com.space.flightserver.service.serviceinterface.auth.AuthOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;

@Service
public class JWTAuthService implements AuthOperations {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthService.class);

    private final UserRepository userRepository;

    private final Duration jwtExpiration;

    private final Algorithm algorithm;

    public JWTAuthService(FlightSecurityProperties securityProperties,
                          UserRepository userRepository) {
        this.userRepository = userRepository;
        var jwtProperties = securityProperties.getJwtProperties();
        this.jwtExpiration = jwtProperties.getAccessExpireIn();
        this.algorithm = Algorithm.HMAC512(new String(jwtProperties.getSecret()).getBytes());
    }

    @Override
    @Transactional
    public AccessTokenResponse getToken(FlightUserDetails userDetails) {
        return response(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private AccessTokenResponse response(String subject,
                                         Collection<? extends GrantedAuthority> authorities) {
        String accessToken = issueJWT(subject, authorities);
        return new AccessTokenResponse(
                accessToken,
                jwtExpiration.toSeconds()
        );
    }

    private String issueJWT(String subject, Collection<? extends GrantedAuthority> authorities) {
        long issuedAt = System.currentTimeMillis();
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(issuedAt))
                .withExpiresAt(new Date(issuedAt + jwtExpiration.toMillis()))
                .withArrayClaim(SecurityConstants.AUTHORITIES_CLAIM, authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toArray(String[]::new))
                .sign(algorithm);
    }
}
