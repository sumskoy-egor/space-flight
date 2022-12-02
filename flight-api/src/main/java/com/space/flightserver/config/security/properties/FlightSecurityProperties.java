package com.space.flightserver.config.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "space-flight.security")
public class FlightSecurityProperties {

    @Valid
    @NestedConfigurationProperty
    private FlightJWTProperties jwtProperties;

    private Map<@NotBlank String, @Valid FlightAdminProperties> admins;

    public FlightJWTProperties getJwtProperties() {
        return jwtProperties;
    }

    public void setJwtProperties(FlightJWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public Map<String, FlightAdminProperties> getAdmins() {
        return admins;
    }

    public void setAdmins(Map<String, FlightAdminProperties> admins) {
        this.admins = admins;
    }
}
