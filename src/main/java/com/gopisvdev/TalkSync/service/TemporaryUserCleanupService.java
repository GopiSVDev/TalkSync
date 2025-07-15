package com.gopisvdev.TalkSync.service;

import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemporaryUserCleanupService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    @Transactional
    public void deleteExpiredTemporaryUsers() {
        List<User> expiredUsers = userRepository.findByIsTemporaryTrueAndExpiresAtBefore(OffsetDateTime.now(ZoneOffset.UTC));

        expiredUsers.forEach(user -> {
            userService.deleteUser(user.getId());
        });

        if (!expiredUsers.isEmpty()) {
            System.out.println("Deleted " + expiredUsers.size() + " expired guest users.");
        }
    }
}
