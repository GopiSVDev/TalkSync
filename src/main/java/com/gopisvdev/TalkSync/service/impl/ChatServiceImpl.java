package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.user.UserResponse;
import com.gopisvdev.TalkSync.entity.Chat;
import com.gopisvdev.TalkSync.entity.ChatParticipant;
import com.gopisvdev.TalkSync.entity.Message;
import com.gopisvdev.TalkSync.entity.User;
import com.gopisvdev.TalkSync.exception.ChatNotFoundException;
import com.gopisvdev.TalkSync.exception.UserNotFoundException;
import com.gopisvdev.TalkSync.repository.*;
import com.gopisvdev.TalkSync.service.interfaces.ChatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final MessageSeenRepository messageSeenRepository;

    private final ChatParticipantRepository chatParticipantRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ChatResponse> getUserChats(UUID userId) {
        List<Chat> chats = chatRepository.findAllByUserId(userId);

        return chats.stream().map(chat -> {
            boolean isGroup = Boolean.TRUE.equals(chat.getIsGroup());


            List<User> otherUsers = chat.getParticipants().stream()
                    .map(ChatParticipant::getUser)
                    .filter(user -> !user.getId().equals(userId))
                    .toList();

            List<UserResponse> participants = otherUsers.stream()
                    .map(user -> new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getName(),
                            user.getAvatarUrl(),
                            user.getIsOnline(),
                            user.getLastSeen()
                    )).toList();

            User otherUser = !isGroup && !otherUsers.isEmpty() ? otherUsers.getFirst() : null;

            String name = isGroup ? chat.getName() : (otherUser != null ? otherUser.getName() : "Unknown");

            String avatarUrl = isGroup ? chat.getAvatarUrl() : (otherUser != null ? otherUser.getAvatarUrl() : null);

            Message lastMessage = chat.getLastMessage();

            return new ChatResponse(
                    chat.getId(),
                    name,
                    avatarUrl,
                    isGroup,
                    LocalDateTime.now(),
                    lastMessage != null ? lastMessage.getContent() : null,
                    lastMessage != null ? lastMessage.getSentAt() : null,
                    participants
            );
        }).toList();
    }

    @Override
    public ChatResponse getPrivateChat(UUID userId, UUID targetUserId) {
        Optional<Chat> existingChat = chatRepository.findDirectChatBetween(userId, targetUserId);

        Chat chat = existingChat.orElseGet(() -> createPrivateChat(userId, targetUserId));

        UUID otherUserId = chat.getParticipants().stream()
                .map(p -> p.getUser().getId())
                .filter(id -> !id.equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Private chat must have 2 users"));

        User otherUser = userRepository.findById(otherUserId).orElseThrow(() -> new UserNotFoundException("User not found"));

        List<UserResponse> participants = List.of(new UserResponse(
                otherUser.getId(),
                otherUser.getUsername(),
                otherUser.getName(),
                otherUser.getAvatarUrl(),
                otherUser.getIsOnline(),
                otherUser.getLastSeen()
        ));

        return ChatResponse.builder()
                .chatId(chat.getId())
                .name(otherUser.getName())
                .avatarUrl(otherUser.getAvatarUrl())
                .isGroup(false)
                .createdAt(chat.getCreatedAt())
                .participants(participants)
                .build();
    }

    @Override
    @Transactional
    public Chat createPrivateChat(UUID userId, UUID targetUserId) {
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
    }

    @Override
    public ChatResponse createGroupChat(CreateGroupChatRequest request, UUID creatorId) {
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
    @Transactional
    public void deleteChat(UUID chatId, UUID requestedBy) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ChatNotFoundException("Chat Not Found"));

        if (chat.getIsGroup()) {
            throw new IllegalArgumentException("This delete method only supports private chats");
        }

        boolean isParticipant = chat.getParticipants().stream().anyMatch((p) -> p.getUser().getId().equals(requestedBy));

        if (!isParticipant) {
            throw new AccessDeniedException("You are not allowed to delete");
        }


        chatRepository.clearLastMessage(chat.getId());

        em.flush();
        em.detach(chat);

        messageSeenRepository.deleteByChatId(chat.getId());
        messageRepository.deleteByChatId(chat.getId());
        chatParticipantRepository.deleteByChatId(chat.getId());

        em.clear();

        chatRepository.deleteById(chat.getId());
    }
}
