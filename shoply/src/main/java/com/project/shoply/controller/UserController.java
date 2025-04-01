package com.project.shoply.controller;

import com.project.shoply.payload.request.LoginRequest;
import com.project.shoply.payload.request.RegistrationRequest;
import com.project.shoply.payload.response.AuthenticationResponse;
import com.project.shoply.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Validated
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("v0/users/registration")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        String result = userService.registerUser(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("v0/users/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        AuthenticationResponse result = userService.login(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
