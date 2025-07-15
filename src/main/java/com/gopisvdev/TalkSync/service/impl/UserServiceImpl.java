package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.user.LoginResponse;
import com.gopisvdev.TalkSync.dto.user.UserRegisterRequest;
import com.gopisvdev.TalkSync.dto.user.UserResponse;
import com.gopisvdev.TalkSync.dto.user.UserUpdateRequest;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.exception.UserNotFoundException;
import com.gopisvdev.TalkSync.exception.UsernameAlreadyExistsException;
import com.gopisvdev.TalkSync.repository.ChatParticipantRepository;
import com.gopisvdev.TalkSync.repository.MessageRepository;
import com.gopisvdev.TalkSync.repository.MessageSeenRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.CustomUserDetails;
import com.gopisvdev.TalkSync.service.JwtService;
import com.gopisvdev.TalkSync.service.UsernameGenerator;
import com.gopisvdev.TalkSync.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
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

    private final UsernameGenerator usernameGenerator;

    @Override
    @Transactional
    public LoginResponse register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .name(request.getDisplayName())
                .isTemporary(false)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);

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
    public LoginResponse login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        user = userDetails.getUser();
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
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .isOnline(user.getIsOnline())
                .lastSeen(user.getLastSeen())
                .build();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UserUpdateRequest updateRequest) {
        User user = userRepository.findById(updateRequest.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateRequest.getDisplayName() != null) {
            user.setName(updateRequest.getDisplayName());
        }

        if (updateRequest.getAvatarUrl() != null) {
            user.setAvatarUrl(updateRequest.getAvatarUrl());
        }

        if (updateRequest.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(updateRequest.getPassword()));
        }

        user = userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .isOnline(user.getIsOnline())
                .lastSeen(user.getLastSeen())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        chatParticipantRepository.deleteAllByUserId(id);
        messageRepository.deleteAllBySender_Id(id);
        messageSeenRepository.deleteAllByUserId(id);

        userRepository.deleteById(id);
    }

    @Override
    public LoginResponse createTemporaryUser() {

        User tempUser;
        String randomName;

        do {
            randomName = usernameGenerator.generateUsername();
        } while (userRepository.existsByUsername(randomName));

        tempUser = User.builder()
                .username(randomName)
                .name(randomName)
                .isTemporary(true)
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(24))
                .build();

        userRepository.save(tempUser);

        tempUser = userRepository.findByUsername(tempUser.getUsername()).get();

        String token = jwtService.generateToken(tempUser);

        UserResponse userResponse = UserResponse.builder()
                .id(tempUser.getId())
                .username(tempUser.getUsername())
                .displayName(tempUser.getName())
                .avatarUrl(tempUser.getAvatarUrl())
                .isOnline(tempUser.getIsOnline())
                .lastSeen(tempUser.getLastSeen())
                .build();

        return new LoginResponse(token, userResponse);
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
        userRepository.findById(userId).ifPresent(user -> {
            user.setIsOnline(isOnline);
            userRepository.save(user);
        });
    }

    @Override
    public void updateLastSeen(UUID userId, OffsetDateTime time) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLastSeen(time);
            userRepository.save(user);
        });
    }
}
