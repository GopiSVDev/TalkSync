package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.message.MessageResponse;
import com.gopisvdev.TalkSync.dto.message.SendMessageRequest;
import com.gopisvdev.TalkSync.entity.Chat;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageResponse sendMessage(SendMessageRequest request) {
        Chat chat = chatRepository.findById(request.getChatId())
                .orElseThrow(() -> new ChatNotFoundException("Chat not found"));

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setSentAt(LocalDateTime.now());

        Message saved = messageRepository.save(message);

        chat.setLastMessage(saved);
        chatRepository.save(chat);

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
