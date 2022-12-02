package com.space.flightserver.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.space.flightserver.config.security.SecurityConstants;
import com.space.flightserver.config.security.properties.FlightSecurityProperties;
import com.space.flightserver.exception.TokenException;
import com.space.flightserver.model.entity.auth.FlightUserDetails;
import com.space.flightserver.model.entity.auth.RefreshToken;
import com.space.flightserver.model.entity.auth.response.AccessTokenResponse;
import com.space.flightserver.model.entity.user.FlightUser;
import com.space.flightserver.repository.RefreshTokenRepository;
import com.space.flightserver.repository.UserRepository;
import com.space.flightserver.service.serviceinterface.auth.AuthOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
public class JWTAuthService implements AuthOperations {

    private static final Logger log = LoggerFactory.getLogger(JWTAuthService.class);

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final Duration jwtExpiration;

    private final Duration refreshExpiration;

    private final Algorithm algorithm;

    public JWTAuthService(FlightSecurityProperties securityProperties,
                          RefreshTokenRepository refreshTokenRepository,
                          UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        var jwtProperties = securityProperties.getJwtProperties();
        this.jwtExpiration = jwtProperties.getAccessExpireIn();
        this.refreshExpiration = jwtProperties.getRefreshExpireIn();
        this.algorithm = Algorithm.HMAC512(new String(jwtProperties.getSecret()).getBytes());
    }

    @Override
    @Transactional
    public AccessTokenResponse getToken(FlightUserDetails userDetails) {
        RefreshToken newToken = issueRefreshToken(userDetails.getSource());
        return response(userDetails.getUsername(), userDetails.getAuthorities(), newToken);
    }

    @Override
    @Transactional
    public AccessTokenResponse refreshToken(String refreshToken) throws TokenException {
        RefreshToken storedToken = refreshTokenRepository.findIfValid(
                verifyRefreshToken(refreshToken),
                OffsetDateTime.now()
        ).orElseThrow(TokenException::new);

        checkIfRotated(storedToken);

        FlightUser user = storedToken.getUser();

        var nextToken = issueRefreshToken(user);

        refreshTokenRepository.updateChain(storedToken, nextToken);

        return response(user.getEmail(), user.getAuthorities().keySet(), nextToken);
    }

    @Override
    @Transactional
    public void invalidateToken(String refreshToken, String ownerEmail) throws TokenException {
        RefreshToken storedToken = refreshTokenRepository.findById(verifyRefreshToken(refreshToken))
                .orElseThrow(TokenException::new);
        checkOwner(storedToken, ownerEmail);
        checkIfRotated(storedToken);
        refreshTokenRepository.deleteChain(storedToken);
    }

    private void checkOwner(RefreshToken storedToken, String email) throws TokenException {
        FlightUser user = storedToken.getUser();
        if(!user.getEmail().equals(email)) {
            // suspend the nasty-ass token pilferer
            String message = "!! INVESTIGATE ASAP !! User {} engaged in a suspicious activity, " +
                    "trying to use a refresh token issued to another user.";
            log.error(message, email);
            // invalidate token
            refreshTokenRepository.deleteChain(storedToken);
            throw new TokenException(message);
        }
    }

    private void checkIfRotated(RefreshToken storedToken) throws TokenException {
        // if an old token is used - we still want to invalidate whole chain in case the new one was stolen
        if (storedToken.getNext() != null) {
            String message = "!! INVESTIGATE ASAP !! An old refresh token used for user {}, " +
                    "signifying possible token theft! Invalidating the entire token chain.";
            log.error(message, storedToken.getUser().getEmail());
            refreshTokenRepository.deleteChain(storedToken.getNext());
            throw new TokenException(message);
        }
    }

    private RefreshToken issueRefreshToken(FlightUser user) {
        var refreshToken = new RefreshToken();
        var now = OffsetDateTime.now();
        refreshToken.setIssuedAt(now);
        refreshToken.setExpireAt(now.plus(refreshExpiration));
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    private AccessTokenResponse response(String subject,
                                         Collection<? extends GrantedAuthority> authorities,
                                         RefreshToken refreshToken) {
        String accessToken = issueJWT(subject, authorities);
        return new AccessTokenResponse(
                accessToken,
                signRefreshToken(refreshToken),
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

    private String signRefreshToken(RefreshToken token) {
        return JWT.create()
                .withSubject(token.getUser().getEmail())
                .withJWTId(token.getValue().toString())
                .withIssuedAt(Date.from(token.getIssuedAt().toInstant()))
                .withExpiresAt(Date.from(token.getExpireAt().toInstant()))
                .sign(algorithm);
    }

    private UUID verifyRefreshToken(String refreshJWT) throws TokenException {
        try {
            String id = JWT.require(algorithm)
                    .build()
                    .verify(refreshJWT)
                    .getId();
            Objects.requireNonNull(id, "jti must be present in refresh token");
            return UUID.fromString(id);
        } catch (Exception e) {
            throw new TokenException(e);
        }
    }

}
