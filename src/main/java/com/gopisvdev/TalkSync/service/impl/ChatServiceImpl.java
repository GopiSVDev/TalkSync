package com.gopisvdev.TalkSync.service.impl;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.ChatSummaryResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;
import com.gopisvdev.TalkSync.service.interfaces.ChatService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatServiceImpl implements ChatService {

    @Override
    public List<ChatSummaryResponse> getUserChats(UUID userId) {
        return List.of();
    }

    @Override
    public ChatResponse getOrCreatePrivateChat(UUID userId, UUID targetUserId) {
        return null;
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
