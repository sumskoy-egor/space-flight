package com.space.flightserver.repository;

import com.space.flightserver.model.entity.user.FlightUserAuthority;
import com.space.flightserver.model.entity.user.KnownAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Stream;

public interface AuthorityRepository extends JpaRepository<FlightUserAuthority, KnownAuthority> {

    Set<KnownAuthority> ADMIN_AUTHORITIES = EnumSet.of(KnownAuthority.ROLE_RECRUITER, KnownAuthority.ROLE_OPERATOR);

    Stream<FlightUserAuthority> findAllByIdIn(Set<KnownAuthority> ids);

}
