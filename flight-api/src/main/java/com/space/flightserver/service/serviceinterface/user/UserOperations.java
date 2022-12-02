package com.space.flightserver.service.serviceinterface.user;

import com.space.flightserver.model.entity.user.request.ChangeUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.MergeUserRequest;
import com.space.flightserver.model.entity.user.request.OverrideUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.SaveUserRequest;
import com.space.flightserver.model.entity.user.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserOperations {

    Page<UserResponse> findAll(Pageable pageable);

    Optional<UserResponse> findById(Long id);

    Optional<UserResponse> findByEmail(String email);

    UserResponse mergeById(Long id, MergeUserRequest request);

    UserResponse mergeByEmail(String email, MergeUserRequest request);

    UserResponse createRecruiter(SaveUserRequest request);

    UserResponse createOperator(SaveUserRequest request);

    UserResponse createAdmin(SaveUserRequest request);

    UserResponse changePasswordById(Long id, OverrideUserPasswordRequest request);

    UserResponse changePasswordByEmail(String email, ChangeUserPasswordRequest request);

    void deleteById(Long id);

    void deleteByEmail(String email);
}
