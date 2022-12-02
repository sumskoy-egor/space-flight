package com.space.flightserver.service.serviceinterface.auth;

import com.space.flightserver.exception.TokenException;
import com.space.flightserver.model.entity.auth.FlightUserDetails;
import com.space.flightserver.model.entity.auth.response.AccessTokenResponse;

public interface AuthOperations {

    AccessTokenResponse getToken(FlightUserDetails userDetails);

    AccessTokenResponse refreshToken(String refreshToken) throws TokenException;

    void invalidateToken(String refreshToken, String ownerEmail) throws TokenException;

}
