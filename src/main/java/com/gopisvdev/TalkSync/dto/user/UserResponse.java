package com.gopisvdev.TalkSync.dto.user;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private Boolean isOnline;
    private LocalDateTime lastSeen;
}
