package com.gopisvdev.TalkSync.service;

import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.repository.ChatParticipantRepository;
import com.gopisvdev.TalkSync.repository.MessageRepository;
import com.gopisvdev.TalkSync.repository.MessageSeenRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TemporaryUserCleanupService {

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final MessageSeenRepository messageSeenRepository;

    private final ChatParticipantRepository chatParticipantRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void deleteExpiredTemporaryUsers() {
        List<User> expiredUsers = userRepository.findByIsTemporaryTrueAndExpiresAtBefore(LocalDateTime.now());

        expiredUsers.forEach(user -> {

            userRepository.deleteById(user.getId());
        });

        if (!expiredUsers.isEmpty()) {
            System.out.println("Deleted " + expiredUsers.size() + " expired guest users.");
        }
    }
}
