package com.gopisvdev.TalkSync.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private Boolean isOnline;
    private OffsetDateTime lastSeen;
}
