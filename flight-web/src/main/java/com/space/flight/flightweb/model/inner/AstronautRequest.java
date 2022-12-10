package com.space.flight.flightweb.model.inner;

public class AstronautRequest {

    private Long id;
    private String name;
    private Boolean isBusy;

    public AstronautRequest() {
    }

    public AstronautRequest(String name, Boolean isBusy, Long id) {
        this.name = name;
        this.isBusy = isBusy;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(Boolean busy) {
        isBusy = busy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
