package com.gopisvdev.TalkSync.dto.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private UUID id;
    private String displayName;
    private String avatarUrl;
    private String password;
}
