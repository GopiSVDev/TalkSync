package com.gopisvdev.TalkSync.dto.message;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSeenNotification {
    private UUID messageId;
    private UUID userId;
    private LocalDateTime seenAt;
    private boolean isAllSeen;
}
