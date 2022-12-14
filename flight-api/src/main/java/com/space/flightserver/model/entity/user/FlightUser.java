package com.space.flightserver.model.entity.user;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "users")
public class FlightUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    @MapKeyEnumerated(EnumType.ORDINAL)
    @MapKey(name = "id")
    private Map<KnownAuthority, FlightUserAuthority> authorities = new EnumMap<>(KnownAuthority.class);

    public FlightUser() {
    }

    public FlightUser(Long id,
                      String email,
                      String name,
                      String password,
                      OffsetDateTime createdAt,
                      Map<KnownAuthority, FlightUserAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightUser that = (FlightUser) o;
        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Map<KnownAuthority, FlightUserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Map<KnownAuthority, FlightUserAuthority> authorities) {
        this.authorities = authorities;
    }
}
