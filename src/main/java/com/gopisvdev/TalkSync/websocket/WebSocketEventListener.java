package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.service.JwtService;
import com.gopisvdev.TalkSync.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserService userService;

    private final JwtService jwtService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        UUID userId = extractUserId(event);
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
        String token = (String) accessor.getSessionAttributes().get("token");
        if (token == null) return null;

        return jwtService.extractUserId(token);
    }
}
