package com.space.flightserver.model.entity.auth.response;

public record AccessTokenResponse(String accessToken, String refreshToken, Long expireIn) {
}
