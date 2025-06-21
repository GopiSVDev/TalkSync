package com.gopisvdev.TalkSync.service;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    User register(String username, String password, String displayName);

    LoginResponse login(String username, String password);

    Optional<User> getUserById(UUID id);

    User updateProfile(UUID id, UserUpdateRequest updateRequest);

    void deleteUser(UUID id);

    User createTemporaryUser();

    void deleteExpiredTemporaryUsers();

    List<User> searchUsers(String query);

    void updateOnlineStatus(UUID userId, boolean isOnline);

    void updateLastSeen(UUID userId, LocalDateTime time);

    boolean isUsernameAvailable(String username);
}
