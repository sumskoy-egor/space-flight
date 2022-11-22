package com.space.flightserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "astronauts")
public class Astronaut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Boolean isBusy = false;

    @ManyToMany(mappedBy = "astronauts")
    private Set<Expedition> expeditions;

    public Astronaut() {
    }

    public Astronaut(String name, Boolean isBusy) {
        this.name = name;
        this.isBusy = isBusy;
    }

    public Astronaut(Long id, String name, Boolean isBusy, Set<Expedition> expeditions) {
        this.id = id;
        this.name = name;
        this.isBusy = isBusy;
        this.expeditions = expeditions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public Set<Expedition> getExpeditions() {
        return expeditions;
    }

    public void setExpeditions(Set<Expedition> expeditions) {
        this.expeditions = expeditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Astronaut astronaut = (Astronaut) o;
        return Objects.equals(id, astronaut.id) && Objects.equals(name, astronaut.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
