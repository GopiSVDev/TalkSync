package com.gopisvdev.TalkSync.dto.message;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendMessageRequest {
    @NotNull
    private UUID chatId;
    @NotNull
    private UUID senderId;
    private String content;
}
