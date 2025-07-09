package com.gopisvdev.TalkSync.dto.message;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMessageRequest {
    private UUID chatId;
    private UUID senderId;
    private String content;
}
