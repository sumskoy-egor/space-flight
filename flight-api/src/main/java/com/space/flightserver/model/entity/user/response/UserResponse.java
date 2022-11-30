package com.space.flightserver.model.entity.user.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.space.flightserver.Routes;
import com.space.flightserver.model.entity.user.FlightUser;
import com.space.flightserver.model.entity.user.KnownAuthority;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        OffsetDateTime createdAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Set<KnownAuthority> authorities
) implements ResourceResponse{

    public static UserResponse fromUser(FlightUser user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                EnumSet.copyOf(user.getAuthorities().keySet())
        );
    }

    public static UserResponse fromUserWithBasicAttributes(FlightUser user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getCreatedAt(),
                null
        );
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String path() {
        return Routes.USERS + "/" + id;
    }
}
