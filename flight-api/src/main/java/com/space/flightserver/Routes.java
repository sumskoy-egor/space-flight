package com.space.flightserver;

public final class Routes {

    private Routes() {
        throw new AssertionError("non-instantiable class");
    }

    public static final String USERS = "/users";

    public static final String TOKEN = "/token";

    public static final String ASTRONAUTS = "/astronauts";

    public static final String EXPEDITIONS = "/expeditions";

    public static final String SPACECRAFTS = "/spacecrafts";
}
