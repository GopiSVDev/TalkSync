package com.gopisvdev.TalkSync.service.interfaces;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;
import com.gopisvdev.TalkSync.entity.Chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatService {

    List<ChatResponse> getUserChats(UUID userId);

    ChatResponse getPrivateChat(UUID userId, UUID targetUserId);

    Chat createPrivateChat(UUID userId, UUID targetUserId);

    ChatResponse createGroupChat(CreateGroupChatRequest request, UUID creatorId);

    ChatResponse getChatById(UUID chatId, UUID userId);

    ChatResponse updateGroupChat(UpdateGroupChatRequest request, UUID userId);

    void addUserToChat(UUID chatId, UUID userIdToAdd, UUID addedBy);

    void removeUserFromChat(UUID chatId, UUID userIdToRemove, UUID removedBy);

    void leaveChat(UUID chatId, UUID userId);

    void deleteChat(UUID chatId, UUID requestedBy);

    Optional<ChatResponse> findPrivateChat(UUID userA, UUID userB);
}
