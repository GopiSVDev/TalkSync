package com.gopisvdev.TalkSync.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TypingStatus {
    private UUID chatId;
    private UUID userId;
    private boolean typing;
}
