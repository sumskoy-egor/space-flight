package com.space.flightserver.model.entity.user;

import org.springframework.security.core.GrantedAuthority;

public enum KnownAuthority implements GrantedAuthority {

    ROLE_RECRUITER,

    ROLE_OPERATOR,

    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
