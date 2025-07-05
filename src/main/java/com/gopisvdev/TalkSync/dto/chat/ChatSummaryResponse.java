package com.gopisvdev.TalkSync.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatSummaryResponse {
    private UUID chatId;
    private String name;
    private String avatarUrl;
    private boolean isGroup;

    private String lastMessage;
    private LocalDateTime lastMessageTime;

    private boolean isOnline;
    private String lastSeen;
}
