package com.gopisvdev.TalkSync.websocket;

import com.gopisvdev.TalkSync.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserService userService;

    private final Map<String, UUID> sessionUserMap = new ConcurrentHashMap<>();
    private final Map<UUID, Set<String>> userSessionsMap = new ConcurrentHashMap<>();

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        UUID userId = extractUserId(event);
        String sessionId = accessor.getSessionId();

        log.info("WebSocket CONNECT: sessionId={}, userId={}", sessionId, userId);

        if (userId != null && sessionId != null) {
            sessionUserMap.put(sessionId, userId);

            userSessionsMap.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);

            if (userSessionsMap.get(userId).size() == 1) {
                userService.updateOnlineStatus(userId, true);
                broadcastStatus(userId, true);
            }
        }
    }

    private void broadcastStatus(UUID userId, boolean isOnline) {
        Map<String, Object> payload = Map.of(
                "userId", userId,
                "online", isOnline
        );

        messagingTemplate.convertAndSend("/topic/user/status", payload);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        UUID userId = sessionUserMap.remove(sessionId);

        log.info("WebSocket DISCONNECT: sessionId={}, userId={}", sessionId, userId);

        if (userId != null) {
            Set<String> sessions = userSessionsMap.get(userId);

            if (sessions != null) {
                sessions.remove(sessionId);

                if (sessions.isEmpty()) {
                    userSessionsMap.remove(userId);
                    userService.updateOnlineStatus(userId, false);
                    userService.updateLastSeen(userId, OffsetDateTime.now(ZoneOffset.UTC));
                    broadcastStatus(userId, false);
                }
            }

        }
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
