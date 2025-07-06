package com.gopisvdev.TalkSync.dto.chat;

import com.gopisvdev.TalkSync.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatResponse {
    private UUID chatId;
    private String name;
    private String avatarUrl;
    private boolean isGroup;
    private LocalDateTime createdAt;

    private String lastMessage;
    private LocalDateTime lastMessageTime;

    private List<UserResponse> participants;
}
