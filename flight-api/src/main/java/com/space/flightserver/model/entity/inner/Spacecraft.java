package com.space.flightserver.model.entity.inner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "spacecrafts")
public class Spacecraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String model;

    private Boolean enabled;

    @OneToMany(mappedBy = "spacecraft")
    private Set<Expedition> expeditions;

    public Spacecraft() {
    }

    public Spacecraft(String model, Boolean enabled) {
        this.model = model;
        this.enabled = enabled;
    }

    public Spacecraft(Long id, String model, Boolean enabled, Set<Expedition> expeditions) {
        this.id = id;
        this.model = model;
        this.enabled = enabled;
        this.expeditions = expeditions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        Spacecraft that = (Spacecraft) o;
        return Objects.equals(id, that.id) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }
}
