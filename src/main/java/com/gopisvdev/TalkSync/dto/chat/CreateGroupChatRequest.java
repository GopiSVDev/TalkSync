package com.gopisvdev.TalkSync.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateGroupChatRequest {
    private String name;
    private String avatarUrl;
    private List<UUID> participantIds;
}
