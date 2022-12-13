package com.space.flight.flightweb.model;

import java.util.UUID;

public class IdDTO {

    private Long idLong;

    private UUID idUUID;

    public IdDTO() {
    }

    public IdDTO(Long idLong) {
        this.idLong = idLong;
    }

    public IdDTO(UUID idUUID) {
        this.idUUID = idUUID;
    }

    public IdDTO(Long idLong, UUID idUUID) {
        this.idLong = idLong;
        this.idUUID = idUUID;
    }

    public Long getIdLong() {
        return idLong;
    }

    public void setIdLong(Long idLong) {
        this.idLong = idLong;
    }

    public UUID getIdUUID() {
        return idUUID;
    }

    public void setIdUUID(UUID idUUID) {
        this.idUUID = idUUID;
    }
}
