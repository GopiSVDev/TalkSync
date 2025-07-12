package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.dto.message.MessageResponse;
import com.gopisvdev.TalkSync.dto.message.MessageSeenNotification;
import com.gopisvdev.TalkSync.dto.message.SeenMessageRequest;
import com.gopisvdev.TalkSync.dto.message.SendMessageRequest;
import com.gopisvdev.TalkSync.service.interfaces.MessageSeenService;
import com.gopisvdev.TalkSync.service.interfaces.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    private final MessageService messageService;
    private final MessageSeenService messageSeenService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(@Valid SendMessageRequest request) {
        MessageResponse response = messageService.sendMessage(request);
        messagingTemplate.convertAndSend("/topic/chat." + request.getChatId(), response);
    }

    @MessageMapping("/chat.seen")
    public void markSeen(@Payload SeenMessageRequest request) {
        var seenNotifications = messageSeenService.markMessagesAsSeen(request);

        var messageIds = seenNotifications.stream()
                .map(MessageSeenNotification::getMessageId)
                .toList();

        var payload = new SeenNotificationPayload(
                request.getChatId(),
                messageIds,
                request.getUserId()
        );

        seenNotifications.forEach(notification ->
                messagingTemplate.convertAndSend(
                        "/topic/chat." + request.getChatId() + ".seen",
                        payload
                )
        );
    }

    public record SeenNotificationPayload(
            UUID chatId,
            List<UUID> messageIds,
            UUID userId
    ) {
    }
}
