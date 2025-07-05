package com.gopisvdev.TalkSync.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateGroupChatRequest {
    private UUID chatId;
    private String name;
    private String avatarUrl;
}
