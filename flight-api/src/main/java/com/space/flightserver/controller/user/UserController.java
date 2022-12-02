package com.space.flightserver.controller.user;

import com.space.flightserver.Routes;
import com.space.flightserver.exception.FlightUserException;
import com.space.flightserver.model.entity.user.request.ChangeUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.MergeUserRequest;
import com.space.flightserver.model.entity.user.request.OverrideUserPasswordRequest;
import com.space.flightserver.model.entity.user.request.SaveUserRequest;
import com.space.flightserver.model.entity.user.response.UserResponse;
import com.space.flightserver.service.serviceinterface.user.UserOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(Routes.USERS)
public class UserController {

    private final UserOperations userOperations;

    public UserController(UserOperations userOperations) {
        this.userOperations = userOperations;
    }

    //region non-admin user

    @PatchMapping(value = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse mergeCurrentUser(@AuthenticationPrincipal String email,
                                      @RequestBody @Valid MergeUserRequest request) {
        return userOperations.mergeByEmail(email, request);
    }

    @PatchMapping("/me/password")
    public UserResponse changeCurrentUserPassword(@AuthenticationPrincipal String email,
                                                  @RequestBody @Valid ChangeUserPasswordRequest request) {
        return userOperations.changePasswordByEmail(email, request);
    }

    @DeleteMapping("/me")
    public void deleteCurrentUser(@AuthenticationPrincipal String email) {
        userOperations.deleteByEmail(email);
    }

    //endregion

    //region admin

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/operator",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse registerOperator(@RequestBody @Valid SaveUserRequest request) {
        return userOperations.createOperator(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/recruiter",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse registerRecruiter(@RequestBody @Valid SaveUserRequest request) {
        return userOperations.createRecruiter(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/admins",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse registerAdmin(@RequestBody @Valid SaveUserRequest request) {
        return userOperations.createAdmin(request);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getCurrentUser(@AuthenticationPrincipal String email) {
        return userOperations.findByEmail(email).orElseThrow(() -> new FlightUserException(HttpStatus.NOT_FOUND,
                "User with such email not found"));
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse mergeUserById(@PathVariable Long id,
                                      @RequestBody @Valid MergeUserRequest request) {
        return userOperations.mergeById(id, request);
    }

    @PatchMapping(value = "/{id}/password",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse changeUserPassword(@PathVariable Long id,
                                           @RequestBody @Valid OverrideUserPasswordRequest request) {
        return userOperations.changePasswordById(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userOperations.deleteById(id);
    }

    //endregion
}
