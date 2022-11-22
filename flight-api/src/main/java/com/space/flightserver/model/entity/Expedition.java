package com.space.flightserver.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "expeditions")
public class Expedition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mission;

    @Column(name = "p_start_date", nullable = false)
    private Instant planStartDate;

    @Column(name = "p_completion_date", nullable = false)
    private Instant planCompletionDate;

    @Column(name = "a_start_date", nullable = false)
    private Instant actualStartDate;

    @Column(name = "a_completion_date", nullable = false)
    private Instant actualCompletionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spacecraft_id", nullable = false)
    private Spacecraft spacecraft;

    @ManyToMany
    @JoinTable(name = "expeditions_astronauts",
            joinColumns = @JoinColumn(name = "expedition_id"),
            inverseJoinColumns = @JoinColumn(name = "astronaut_id")
    )
    private Set<Astronaut> astronauts;

    public Expedition() {
    }

    public Expedition(String mission, Instant startDate, Instant completionDate, Spacecraft spacecraft, Set<Astronaut> astronauts) {
        this.mission = mission;
        this.planStartDate = startDate;
        this.planCompletionDate = completionDate;
        this.spacecraft = spacecraft;
        this.astronauts = astronauts;
    }

    public Expedition(Long id, String mission, Instant startDate, Instant completionDate, Spacecraft spacecraft, Set<Astronaut> astronauts) {
        this.id = id;
        this.mission = mission;
        this.planStartDate = startDate;
        this.planCompletionDate = completionDate;
        this.spacecraft = spacecraft;
        this.astronauts = astronauts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public Instant getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Instant planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Instant getPlanCompletionDate() {
        return planCompletionDate;
    }

    public void setPlanCompletionDate(Instant planCompletionDate) {
        this.planCompletionDate = planCompletionDate;
    }

    public Instant getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(Instant actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public Instant getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(Instant actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public void setSpacecraft(Spacecraft spacecraft) {
        this.spacecraft = spacecraft;
    }

    public Set<Astronaut> getAstronauts() {
        return astronauts;
    }

    public void setAstronauts(Set<Astronaut> astronauts) {
        this.astronauts = astronauts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expedition that = (Expedition) o;
        return Objects.equals(id, that.id) && Objects.equals(planStartDate, that.planStartDate) && Objects.equals(planCompletionDate, that.planCompletionDate) && Objects.equals(spacecraft, that.spacecraft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planStartDate, planCompletionDate, spacecraft);
    }
}
