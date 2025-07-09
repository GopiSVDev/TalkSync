package com.gopisvdev.TalkSync.dto.message;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedMessagesResponse {
    private List<MessageResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private boolean hasNext;
}
