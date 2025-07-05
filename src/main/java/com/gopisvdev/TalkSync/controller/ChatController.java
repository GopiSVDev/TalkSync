package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.chat.ChatResponse;
import com.gopisvdev.TalkSync.dto.chat.ChatSummaryResponse;
import com.gopisvdev.TalkSync.service.interfaces.ChatService;
import com.gopisvdev.TalkSync.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatSummaryResponse>> getUserChats() {
        UUID userId = AuthUtil.getCurrentUserId();
        return ResponseEntity.ok(chatService.getUserChats(userId));
    }

    @PostMapping("/private")
    public ResponseEntity<ChatResponse> getOrCreatePrivateChat(@RequestParam UUID userId, @RequestParam UUID targetUserId) {
        return ResponseEntity.ok(chatService.getOrCreatePrivateChat(userId, targetUserId));
    }
}
