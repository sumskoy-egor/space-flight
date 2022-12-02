package com.space.flightserver.model.entity.auth;

import com.space.flightserver.model.entity.user.FlightUser;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue
    private UUID value;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FlightUser user;

    @Column(name = "issued_at", nullable = false)
    private OffsetDateTime issuedAt;

    @Column(name = "expire_at", nullable = false)
    private OffsetDateTime expireAt;

    @OneToMany(mappedBy = "next", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshToken> previous = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next")
    @Access(AccessType.PROPERTY)
    private RefreshToken next;

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }

    public FlightUser getUser() {
        return user;
    }

    public void setUser(FlightUser user) {
        this.user = user;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(OffsetDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public OffsetDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(OffsetDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public List<RefreshToken> getPrevious() {
        return previous;
    }

    public void setPrevious(List<RefreshToken> previous) {
        this.previous = previous;
    }

    public RefreshToken getNext() {
        return next;
    }

    public void setNext(RefreshToken next) {
        this.next = next;
    }
}
