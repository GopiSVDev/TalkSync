package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserResponse;
import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.exception.UserNotFoundException;
import com.gopisvdev.TalkSync.exception.UsernameAlreadyExistsException;
import com.gopisvdev.TalkSync.repository.ChatParticipantRepository;
import com.gopisvdev.TalkSync.repository.MessageRepository;
import com.gopisvdev.TalkSync.repository.MessageSeenRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.JwtService;
import com.gopisvdev.TalkSync.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final MessageSeenRepository messageSeenRepository;

    private final ChatParticipantRepository chatParticipantRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public User register(String username, String password, String displayName) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .name(displayName)
                .isTemporary(false)
                .build();

        return userRepository.save(user);
    }

    @Override
    public LoginResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token = jwtService.generateToken(username);

        User user = (User) authentication.getPrincipal();
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .isOnline(user.getIsOnline())
                .lastSeen(user.getLastSeen())
                .build();

        return new LoginResponse(token, userResponse);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }


    @Override
    public User updateProfile(UUID id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateRequest.getDisplayName() != null) {
            user.setName(updateRequest.getDisplayName());
        }

        if (updateRequest.getAvatarUrl() != null) {
            user.setAvatarUrl(updateRequest.getAvatarUrl());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        messageRepository.deleteById(id);
        messageSeenRepository.deleteById(id);
        chatParticipantRepository.deleteById(id);
        userRepository.deleteById(id);
    }

    @Override
    public User createTemporaryUser() {
        String randomName = "Guest" + UUID.randomUUID().toString().substring(0, 8);

        User tempUser = User.builder()
                .username("guest_" + UUID.randomUUID())
                .name(randomName)
                .isTemporary(true)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();

        return userRepository.save(tempUser);
    }

    @Override
    public List<UserResponse> searchUsers(String query) {
        List<User> users = userRepository.searchUsers(query);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthenticated");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        UUID id = user.getId();

        return users.stream()
                .filter(u -> !u.getId().equals(id))
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getName(),
                        u.getAvatarUrl(),
                        u.getIsOnline(),
                        u.getLastSeen()
                )).collect(Collectors.toList());
    }

    @Override
    public void updateOnlineStatus(UUID userId, boolean isOnline) {

    }

    @Override
    public void updateLastSeen(UUID userId, LocalDateTime time) {

    }
}
