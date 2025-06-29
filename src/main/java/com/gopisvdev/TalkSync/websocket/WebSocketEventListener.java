package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        UUID userId = extractUserId(event);

        if (userId == null) {
            System.out.println("🚫 userId is null in session attributes");
            return;
        }

        userService.updateOnlineStatus(userId, true);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        UUID userId = extractUserId(event);
        userService.updateOnlineStatus(userId, false);
        userService.updateLastSeen(userId, LocalDateTime.now());
    }

    private UUID extractUserId(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if (sessionAttributes == null) {
            System.err.println("Session Attributes are null");
            return null;
        }

        UUID userId = (UUID) accessor.getSessionAttributes().get("userId");

        if (userId == null) {
            System.err.println("userId is null in session attributes");
        }

        return userId;
    }
}
