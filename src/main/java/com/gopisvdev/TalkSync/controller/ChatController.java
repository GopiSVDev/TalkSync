package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.service.interfaces.ChatService;
import com.gopisvdev.TalkSync.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getUserChats() {
        UUID userId = AuthUtil.getCurrentUserId();
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @PostMapping("/private")
    public ResponseEntity<ChatResponse> getOrCreatePrivateChat(@RequestParam UUID userId, @RequestParam UUID targetUserId) {
        return ResponseEntity.ok(chatService.getPrivateChat(userId, targetUserId));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID chatId) throws AccessDeniedException {
        UUID userId = AuthUtil.getCurrentUserId();
        chatService.deleteChat(chatId, userId);
        return ResponseEntity.noContent().build();
    }
}
