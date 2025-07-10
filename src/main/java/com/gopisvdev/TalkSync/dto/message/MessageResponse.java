package com.gopisvdev.TalkSync.dto.message;

import com.gopisvdev.TalkSync.entity.Message;
import com.gopisvdev.TalkSync.entity.MessageSeen;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private List<MessageSeen> seenBy;

    public static MessageResponse from(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getChat().getId(),
                message.getSender().getId(),
                message.getContent(),
                message.getMediaUrl(),
                message.getMediaType() != null ? message.getMediaType().name() : null,
                message.getCreatedAt(),
                message.getSentAt(),
                message.getSeenBy()
        );
    }
}
