package com.gopisvdev.TalkSync.service.interfaces;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.ChatSummaryResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatService {

    List<ChatSummaryResponse> getUserChats(UUID userId);

    ChatResponse getOrCreatePrivateChat(UUID userId, UUID targetUserId);

    ChatResponse createGroupChat(CreateGroupChatRequest request, UUID creatorId);

    ChatResponse getChatById(UUID chatId, UUID userId);

    ChatResponse updateGroupChat(UpdateGroupChatRequest request, UUID userId);

    void addUserToChat(UUID chatId, UUID userIdToAdd, UUID addedBy);

    void removeUserFromChat(UUID chatId, UUID userIdToRemove, UUID removedBy);

    void leaveChat(UUID chatId, UUID userId);

    void deleteChat(UUID chatId, UUID requestedBy);

    Optional<ChatResponse> findPrivateChat(UUID userA, UUID userB);
}
