package com.gopisvdev.TalkSync.service;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserRegisterRequest;
import com.gopisvdev.TalkSync.dto.user.UserResponse;
import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    LoginResponse register(UserRegisterRequest request);

    LoginResponse login(String username, String password);

    Optional<User> getUserById(UUID id);

    User updateProfile(UUID id, UserUpdateRequest updateRequest);

    void deleteUser(UUID id);

    LoginResponse createTemporaryUser();

    List<UserResponse> searchUsers(String query);

    void updateOnlineStatus(UUID userId, boolean isOnline);

    void updateLastSeen(UUID userId, LocalDateTime time);
}
