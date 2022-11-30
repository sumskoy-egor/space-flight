package com.space.flightserver.model.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "authorities")
public class FlightUserAuthority {

    @Id
    @Column(nullable = false, unique = true)
    private KnownAuthority id;

    @ManyToMany(mappedBy = "authorities")
    private Set<FlightUser> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightUserAuthority that = (FlightUserAuthority) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public KnownAuthority getId() {
        return id;
    }

    public void setId(KnownAuthority id) {
        this.id = id;
    }

    public Set<FlightUser> getUsers() {
        return users;
    }

    public void setUsers(Set<FlightUser> users) {
        this.users = users;
    }
}
