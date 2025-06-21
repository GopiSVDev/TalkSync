package com.gopisvdev.TalkSync.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String displayName;
    private String avatarUrl;
}
