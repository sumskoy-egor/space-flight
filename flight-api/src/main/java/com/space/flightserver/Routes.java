package com.space.flightserver;

public final class Routes {

    private Routes() {
        throw new AssertionError("non-instantiable class");
    }

    public static final String API_ROOT = "/api/v3";

    public static final String USERS = API_ROOT + "/users";

    public static final String TOKEN = API_ROOT + "/token";

    public static final String ASTRONAUTS = API_ROOT + "/astronauts";

    public static final String EXPEDITIONS = API_ROOT + "/expeditions";

    public static final String SPACECRAFTS = API_ROOT + "/spacecrafts";
}
