package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User register(String username, String password, String displayName) {
        return null;
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User getUserById(UUID id) {
        return null;
    }
    

    @Override
    public User updateProfile(UUID id, UserUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public void deleteUser(UUID id) {

    }

    @Override
    public User createTemporaryUser() {
        return null;
    }

    @Override
    public void deleteExpiredTemporaryUsers() {

    }

    @Override
    public List<User> searchUsers(String query) {
        return List.of();
    }

    @Override
    public void updateOnlineStatus(UUID userId, boolean isOnline) {

    }

    @Override
    public void updateLastSeen(UUID userId, LocalDateTime time) {

    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return false;
    }
}
