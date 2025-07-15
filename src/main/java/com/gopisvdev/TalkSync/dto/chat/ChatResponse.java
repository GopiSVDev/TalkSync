package com.gopisvdev.TalkSync.dto.chat;

import com.gopisvdev.TalkSync.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
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

    @CreationTimestamp
    private OffsetDateTime createdAt;

    private String lastMessage;
    private OffsetDateTime lastMessageTime;

    private List<UserResponse> participants;
}
