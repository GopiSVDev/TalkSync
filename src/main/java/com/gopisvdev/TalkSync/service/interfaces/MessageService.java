package com.gopisvdev.TalkSync.service.interfaces;

import com.gopisvdev.TalkSync.dto.message.MessageResponse;
import com.gopisvdev.TalkSync.dto.message.SendMessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MessageService {
    MessageResponse sendMessage(SendMessageRequest sendMessageRequest);

    Page<MessageResponse> getMessages(UUID chatId, Pageable pageable);

    List<MessageResponse> searchMessages(UUID chatId, String keyword);

    MessageResponse getLastMessage(UUID chatId);

    void deleteMessagesByUser(UUID userId);
}
