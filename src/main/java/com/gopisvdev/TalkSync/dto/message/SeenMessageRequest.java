package com.gopisvdev.TalkSync.dto.message;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeenMessageRequest {
    private UUID chatId;
    private UUID userId;
    private List<UUID> messageIds;
}
