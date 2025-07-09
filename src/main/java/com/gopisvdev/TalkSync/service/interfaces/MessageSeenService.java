package com.gopisvdev.TalkSync.service.interfaces;

import com.gopisvdev.TalkSync.dto.message.MessageSeenNotification;
import com.gopisvdev.TalkSync.dto.message.SeenMessageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MessageSeenService {
    List<MessageSeenNotification> markMessagesAsSeen(SeenMessageRequest request);

    List<UUID> getSeenUserIds(UUID messageId);

    boolean hasUserSeen(UUID messageId, UUID userId);

    void deleteAllSeenByUser(UUID userId);
}
