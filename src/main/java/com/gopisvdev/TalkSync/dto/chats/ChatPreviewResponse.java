package com.gopisvdev.TalkSync.dto.chats;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatPreviewResponse {
    private UUID id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private Boolean isOnline;
    private LocalDateTime lastSeen;


    private String lastMessage;
    private LocalDateTime time;
}
