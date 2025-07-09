package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.dto.chat.TypingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class TypingSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingStatus typingStatus) {
        System.out.println("Typing status received: " + typingStatus);
        UUID chatId = typingStatus.getChatId();
        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/typing", typingStatus);
    }
}
