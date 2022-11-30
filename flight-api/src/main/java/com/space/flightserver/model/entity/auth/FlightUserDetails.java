package com.space.flightserver.model.entity.auth;

import com.space.flightserver.model.entity.user.FlightUser;
import org.springframework.security.core.userdetails.User;

import java.util.EnumSet;

public class FlightUserDetails extends User {

    private final FlightUser source;

    public FlightUserDetails(FlightUser source) {
        super(source.getEmail(),
                source.getPassword(),
                EnumSet.copyOf(source.getAuthorities().keySet())
        );
        this.source = source;
    }

    public FlightUser getSource() {
        return source;
    }
}
