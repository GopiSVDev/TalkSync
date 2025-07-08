package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.dto.chat.TypingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class TypingSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void handleTyping(@Payload TypingStatus typingStatus) {
        UUID chatId = typingStatus.getChatId();
        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/typing", typingStatus);
    }
}
