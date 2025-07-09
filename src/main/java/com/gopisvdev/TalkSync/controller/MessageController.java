package com.gopisvdev.TalkSync.controller;

import com.gopisvdev.TalkSync.dto.message.MessageResponse;
import com.gopisvdev.TalkSync.dto.message.PaginatedMessagesResponse;
import com.gopisvdev.TalkSync.dto.message.SendMessageRequest;
import com.gopisvdev.TalkSync.service.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageResponse sendMessage(@RequestBody SendMessageRequest request) {
        return messageService.sendMessage(request);
    }


    @GetMapping("/{chatId}")
    public PaginatedMessagesResponse getMessages(
            @PathVariable UUID chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> messagePage = messageService.getMessages(chatId, pageable);
        return new PaginatedMessagesResponse(
                messagePage.getContent(),
                messagePage.getNumber(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.hasNext()
        );
    }


    @GetMapping("/{chatId}/search")
    public List<MessageResponse> searchMessages(
            @PathVariable UUID chatId,
            @RequestParam String keyword
    ) {
        return messageService.searchMessages(chatId, keyword);
    }
    
    @GetMapping("/{chatId}/last")
    public MessageResponse getLastMessage(@PathVariable UUID chatId) {
        return messageService.getLastMessage(chatId);
    }
}

