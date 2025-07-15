package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.message.MessageResponse;
import com.gopisvdev.TalkSync.dto.message.SendMessageRequest;
import com.gopisvdev.TalkSync.entity.Chat;
import com.gopisvdev.TalkSync.entity.ChatParticipant;
import com.gopisvdev.TalkSync.entity.Message;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.exception.ChatNotFoundException;
import com.gopisvdev.TalkSync.exception.UserNotFoundException;
import com.gopisvdev.TalkSync.repository.ChatRepository;
import com.gopisvdev.TalkSync.repository.MessageRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request) {

        if (request.getChatId() == null) {
            throw new IllegalArgumentException("Chat ID must not be null");
        }
        if (request.getSenderId() == null) {
            throw new IllegalArgumentException("Sender ID must not be null");
        }

        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setSentAt(OffsetDateTime.now(ZoneOffset.UTC));

        Message saved = messageRepository.save(message);

        chat.setLastMessage(saved);
        chatRepository.save(chat);

        List<UUID> participantsIds = chat.getParticipants().stream()
                .map(ChatParticipant::getUser)
                .map(User::getId)
                .toList();

        for (UUID participantId : participantsIds) {
            messagingTemplate.convertAndSend("/topic/chat.refresh." + participantId, "refresh");
        }

        return MessageResponse.from(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageResponse> getMessages(UUID chatId, Pageable pageable) {
        Page<Message> page = messageRepository.findByChatIdOrderByCreatedAtDesc(chatId, pageable);
        return page.map(MessageResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> searchMessages(UUID chatId, String keyword) {
        return messageRepository.findByChatIdAndContentContainingIgnoreCase(chatId, keyword)
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MessageResponse getLastMessage(UUID chatId) {
        return messageRepository.findLastMessageByChatId(chatId)
                .map(MessageResponse::from)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteMessagesByUser(UUID userId) {
        messageRepository.deleteAllBySender_Id(userId);
    }
}
