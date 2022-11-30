package com.space.flightserver.repository;

import com.space.flightserver.model.entity.user.FlightUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<FlightUser, Long> {

    Optional<FlightUser> findByEmail(String email);

    Optional<FlightUser> findByEmailOrName(String email, String name);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
}
