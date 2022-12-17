package com.space.flightserver.service.serviceinterface.auth;

import com.space.flightserver.model.entity.auth.FlightUserDetails;
import com.space.flightserver.model.entity.auth.response.AccessTokenResponse;

public interface AuthOperations {

    AccessTokenResponse getToken(FlightUserDetails userDetails);

}
