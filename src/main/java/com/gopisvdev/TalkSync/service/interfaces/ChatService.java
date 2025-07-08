package com.gopisvdev.TalkSync.service.interfaces;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.CreateGroupChatRequest;
import com.gopisvdev.TalkSync.dto.chat.UpdateGroupChatRequest;
import com.gopisvdev.TalkSync.entity.Chat;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ChatResponse> getUserChats(UUID userId);

    ChatResponse getPrivateChat(UUID userId, UUID targetUserId);

    Chat createPrivateChat(UUID userId, UUID targetUserId);

    ChatResponse createGroupChat(CreateGroupChatRequest request, UUID creatorId);

    ChatResponse updateGroupChat(UpdateGroupChatRequest request, UUID userId);

    void addUserToChat(UUID chatId, UUID userIdToAdd, UUID addedBy);

    void removeUserFromChat(UUID chatId, UUID userIdToRemove, UUID removedBy);

    void leaveChat(UUID chatId, UUID userId);

    void deleteChat(UUID chatId, UUID requestedBy) throws AccessDeniedException;
}
