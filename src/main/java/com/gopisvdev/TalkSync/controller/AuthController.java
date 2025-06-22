package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserLoginRequest;
import com.gopisvdev.TalkSync.dto.user.UserRegisterRequest;
import com.gopisvdev.TalkSync.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String error = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        return new ResponseEntity<>(userService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest loginRequest, BindingResult result) {

        if (result.hasErrors()) {
            String error = Objects.requireNonNull(result.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }

        return ResponseEntity.ok(userService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/guest")
    public ResponseEntity<LoginResponse> guestLogin() {
        return new ResponseEntity<>(userService.createTemporaryUser(), HttpStatus.CREATED);
    }
}
