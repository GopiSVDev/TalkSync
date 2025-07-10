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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    public void markSeen(SeenMessageRequest request) {
        var seenNotifications = messageSeenService.markMessagesAsSeen(request);
        for (MessageSeenNotification n : seenNotifications) {
            messagingTemplate.convertAndSend("/topic/chat." + request.getChatId() + ".seen", n);
        }
    }
}
