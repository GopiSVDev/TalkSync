package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserLoginRequest;
import com.gopisvdev.TalkSync.dto.user.UserRegisterRequest;
import com.gopisvdev.TalkSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody UserRegisterRequest request) {
        return new ResponseEntity<>(userService.register(request.getUsername(), request.getPassword(), request.getDisplayName()), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest.getUsername(), loginRequest.getPassword()));
    }

    @PostMapping("/guest")
    public ResponseEntity<LoginResponse> guestLogin() {
        return new ResponseEntity<>(userService.createTemporaryUser(), HttpStatus.CREATED);
    }
}
