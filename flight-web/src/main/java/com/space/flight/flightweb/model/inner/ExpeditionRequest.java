package com.space.flight.flightweb.model.inner;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ExpeditionRequest {

    private String mission;

    private String startDate;

    private String completionDate;

    private Long spacecraft_id;

    private Set<Long> astronauts_id;

    private String astronautsIdString;

    public ExpeditionRequest() {
    }

    public ExpeditionRequest(String mission,
                             String startDate,
                             String completionDate,
                             Long spacecraftId,
                             Set<Long> astronautsId) {
        this.mission = mission;
        this.startDate = startDate;
        this.completionDate = completionDate;
        this.spacecraft_id = spacecraftId;
        this.astronauts_id = astronautsId;
    }

    public String getMission() {
        return mission;
    }

    public void parseAstronauts() {
        this.astronauts_id = Arrays.stream(this.astronautsIdString
                .trim().split(", "))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public void setDateFormat() {
        this.startDate += "T00:00:00.000Z";
        this.completionDate += "T00:00:00.000Z";
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }

    public Long getSpacecraft_id() {
        return spacecraft_id;
    }

    public void setSpacecraft_id(Long spacecraftId) {
        this.spacecraft_id = spacecraftId;
    }

    public Set<Long> getAstronauts_id() {
        return astronauts_id;
    }

    public void setAstronauts_id(Set<Long> astronautsId) {
        this.astronauts_id = astronautsId;
    }

    public String getAstronautsIdString() {
        return astronautsIdString;
    }

    public void setAstronautsIdString(String astronautsIdString) {
        this.astronautsIdString = astronautsIdString;
    }
}
