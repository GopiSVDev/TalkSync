package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.ChatSummaryResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;
import com.gopisvdev.TalkSync.entity.Chat;
import com.gopisvdev.TalkSync.entity.ChatParticipant;
import com.gopisvdev.TalkSync.entity.Message;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.exception.UserNotFoundException;
import com.gopisvdev.TalkSync.repository.ChatRepository;
import com.gopisvdev.TalkSync.repository.UserRepository;
import com.gopisvdev.TalkSync.service.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    @Override
    public List<ChatSummaryResponse> getUserChats(UUID userId) {
        List<Chat> chats = chatRepository.findAllByUserId(userId);

        return chats.stream().map(chat -> {
            boolean isGroup = Boolean.TRUE.equals(chat.getIsGroup());

            List<User> otherUsers = chat.getParticipants().stream()
                    .map(ChatParticipant::getUser)
                    .filter(user -> !user.getId().equals(userId))
                    .toList();

            User otherUser = !isGroup && !otherUsers.isEmpty() ? otherUsers.getFirst() : null;

            String name = isGroup ? chat.getName() : (otherUser != null ? otherUser.getName() : "Unknown");

            String avatarUrl = isGroup ? chat.getAvatarUrl() : (otherUser != null ? otherUser.getAvatarUrl() : null);

            Message lastMessage = chat.getLastMessage();

            return new ChatSummaryResponse(
                    chat.getId(),
                    name,
                    avatarUrl,
                    isGroup,
                    lastMessage != null ? lastMessage.getContent() : null,
                    lastMessage != null ? lastMessage.getSentAt() : null,
                    otherUser != null && Boolean.TRUE.equals(otherUser.getIsOnline()),
                    otherUser != null && otherUser.getLastSeen() != null ? otherUser.getLastSeen().toString() : null
            );
        }).toList();
    }

    @Override
    public ChatResponse getOrCreatePrivateChat(UUID userId, UUID targetUserId) {
        Optional<Chat> existingChat = chatRepository.findDirectChatBetween(userId, targetUserId);

        Chat chat = existingChat.orElseGet(() -> {
            Chat newChat = Chat.builder().isGroup(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            User targetUser = userRepository.findById(targetUserId).orElseThrow(() -> new UserNotFoundException("User not found"));

            ChatParticipant participant1 = ChatParticipant.builder().chat(newChat).user(user).build();

            ChatParticipant participant2 = ChatParticipant.builder().chat(newChat).user(targetUser).build();

            newChat.getParticipants().add(participant1);
            newChat.getParticipants().add(participant2);

            return chatRepository.save(newChat);
        });

        UUID otherUserId = chat.getParticipants().stream()
                .map(p -> p.getUser().getId())
                .filter(id -> !id.equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Private chat must have 2 users"));

        User otherUser = userRepository.findById(otherUserId).orElseThrow(() -> new UserNotFoundException("User not found"));

        return ChatResponse.builder()
                .chatId(chat.getId())
                .name(chat.getName())
                .avatarUrl(chat.getAvatarUrl())
                .isGroup(false)
                .createdAt(chat.getCreatedAt())
                .build();
    }

    @Override
    public ChatResponse createGroupChat(CreateGroupChatRequest request, UUID creatorId) {
        return null;
    }

    @Override
    public ChatResponse getChatById(UUID chatId, UUID userId) {
        return null;
    }

    @Override
    public ChatResponse updateGroupChat(UpdateGroupChatRequest request, UUID userId) {
        return null;
    }

    @Override
    public void addUserToChat(UUID chatId, UUID userIdToAdd, UUID addedBy) {

    }

    @Override
    public void removeUserFromChat(UUID chatId, UUID userIdToRemove, UUID removedBy) {

    }

    @Override
    public void leaveChat(UUID chatId, UUID userId) {

    }

    @Override
    public void deleteChat(UUID chatId, UUID requestedBy) {

    }

    @Override
    public Optional<ChatResponse> findPrivateChat(UUID userA, UUID userB) {
        return Optional.empty();
    }
}
