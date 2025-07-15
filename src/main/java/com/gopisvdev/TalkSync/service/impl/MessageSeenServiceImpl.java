package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.message.MessageSeenNotification;
import com.gopisvdev.TalkSync.dto.message.SeenMessageRequest;
import com.gopisvdev.TalkSync.entity.Message;
import com.gopisvdev.TalkSync.entity.MessageSeen;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.repository.MessageRepository;
import com.gopisvdev.TalkSync.repository.MessageSeenRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.interfaces.MessageSeenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageSeenServiceImpl implements MessageSeenService {
    private final MessageSeenRepository messageSeenRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<MessageSeenNotification> markMessagesAsSeen(SeenMessageRequest request) {
        UUID userId = request.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<MessageSeenNotification> notifications = new ArrayList<>();

        for (UUID messageId : request.getMessageIds()) {
            if (!messageSeenRepository.existsByMessageIdAndUserId(messageId, userId)) {
                Message message = messageRepository.findById(messageId)
                        .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));

                MessageSeen seen = new MessageSeen();
                seen.setMessage(message);
                seen.setUser(user);
                seen.setSeenAt(OffsetDateTime.now(ZoneOffset.UTC));

                messageSeenRepository.save(seen);

                notifications.add(MessageSeenNotification.builder()
                        .messageId(messageId)
                        .userId(userId)
                        .seenAt(seen.getSeenAt())
                        .build());
            }
        }

        return notifications;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> getSeenUserIds(UUID messageId) {
        return messageSeenRepository.findSeenUsersByMessageId(messageId)
                .stream()
                .map(User::getId)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserSeen(UUID messageId, UUID userId) {
        return messageSeenRepository.existsByMessageIdAndUserId(messageId, userId);
    }

    @Override
    @Transactional
    public void deleteAllSeenByUser(UUID userId) {
        messageSeenRepository.deleteAllByUserId(userId);
    }
}
