package com.space.flightserver.service.user;

import com.space.flightserver.exception.FlightUserException;
import com.space.flightserver.model.entity.auth.FlightUserDetails;
import com.space.flightserver.model.entity.user.FlightUser;
import com.space.flightserver.model.entity.user.FlightUserAuthority;
import com.space.flightserver.model.entity.user.KnownAuthority;
import com.space.flightserver.model.entity.user.request.ChangeUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.MergeUserRequest;
import com.space.flightserver.model.entity.user.request.OverrideUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.SaveUserRequest;
import com.space.flightserver.model.entity.user.response.UserResponse;
import com.space.flightserver.repository.AuthorityRepository;
import com.space.flightserver.repository.UserRepository;
import com.space.flightserver.service.serviceinterface.user.UserOperations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, UserOperations {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FlightUser user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));

        return new FlightUserDetails(user);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponse::fromUserWithBasicAttributes);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findById(Long id) {
        return userRepository.findById(id).map(UserResponse::fromUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponse> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserResponse::fromUser);
    }

    @Override
    @Transactional
    public UserResponse mergeById(Long id, MergeUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                "User with id " + id + " was not found"));

        if(userRepository.existsByEmail(request.email())) {
            throw new FlightUserException(HttpStatus.CONFLICT, "Duplicated email!");
        }

        user.setEmail(request.email());
        user.setName(request.name());

        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse mergeByEmail(String email, MergeUserRequest request) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                "User with such email was not found"));

        if(userRepository.existsByEmail(request.email())) {
            throw new FlightUserException(HttpStatus.CONFLICT, "Duplicated email!");
        }

        user.setEmail(request.email());
        user.setName(request.name());

        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse createRecruiter(SaveUserRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new FlightUserException(HttpStatus.CONFLICT, "Duplicated email!");
        }

        var authority = authorityRepository
                .findById(KnownAuthority.ROLE_RECRUITER)
                .orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                        "Authority " + KnownAuthority.ROLE_RECRUITER.name() + " was not found"));
        Map<KnownAuthority, FlightUserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_RECRUITER, authority);

        return UserResponse.fromUser(createUser(request, authorities));
    }

    @Override
    @Transactional
    public UserResponse createOperator(SaveUserRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new FlightUserException(HttpStatus.CONFLICT, "Duplicated email!");
        }

        var authority = authorityRepository
                .findById(KnownAuthority.ROLE_OPERATOR)
                .orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                        "Authority " + KnownAuthority.ROLE_OPERATOR.name() + " was not found"));
        Map<KnownAuthority, FlightUserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_OPERATOR, authority);

        return UserResponse.fromUser(createUser(request, authorities));
    }

    @Override
    public UserResponse createAdmin(SaveUserRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new FlightUserException(HttpStatus.CONFLICT, "Duplicated email!");
        }

        var authority = authorityRepository
                .findById(KnownAuthority.ROLE_ADMIN)
                .orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                        "Authority " + KnownAuthority.ROLE_OPERATOR.name() + " was not found"));

        Map<KnownAuthority, FlightUserAuthority> authorities = new EnumMap<>(KnownAuthority.class);
        authorities.put(KnownAuthority.ROLE_ADMIN, authority);

        return UserResponse.fromUser(createUser(request, authorities));
    }

    @Override
    @Transactional
    public UserResponse changePasswordById(Long id, OverrideUserPasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                "User with id " + id + " was not found"));

        user.setPassword(passwordEncoder.encode(request.password()));
        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                "User with such email was not found"));

        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword()))
            throw new FlightUserException(HttpStatus.BAD_REQUEST, "Wrong password!");
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        return UserResponse.fromUser(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if(!userRepository.existsById(id))
            throw new FlightUserException(HttpStatus.NOT_FOUND, "User with id " + id + " was not found");
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        if(!userRepository.existsByEmail(email))
            throw new FlightUserException(HttpStatus.NOT_FOUND, "User with such email was not found");
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void mergeAdmins(List<SaveUserRequest> requests) {
        if(requests.isEmpty()) return;
        Map<KnownAuthority, FlightUserAuthority> authorities = authorityRepository
                .findAllByIdIn(AuthorityRepository.ADMIN_AUTHORITIES)
                .collect(Collectors.toMap(
                        FlightUserAuthority::getId,
                        Function.identity(),
                        (e1, e2) -> e2,
                        () -> new EnumMap<>(KnownAuthority.class)
                ));

        for (SaveUserRequest request : requests) {
            var user = userRepository.findByEmail(request.email()).orElseGet(() -> {
                var newUser = new FlightUser();
                newUser.setCreatedAt(OffsetDateTime.now());
                newUser.setEmail(request.email());
                return newUser;
            });
            user.setName(request.name());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.getAuthorities().putAll(authorities);
            userRepository.save(user);
        }
    }

    private FlightUser createUser(SaveUserRequest request, Map<KnownAuthority, FlightUserAuthority> authorities) {
        var user = new FlightUser();
        user.getAuthorities().putAll(authorities);
        user.setEmail(request.email());
        user.setName(request.name());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(OffsetDateTime.now());
        userRepository.save(user);

        return user;
    }
}
