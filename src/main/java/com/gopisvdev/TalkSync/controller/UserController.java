package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.user.UserResponse;
import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserResponse>> searchUsers(@PathVariable String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }
}
