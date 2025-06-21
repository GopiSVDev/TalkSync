package com.gopisvdev.TalkSync.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "Username can't be empty")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
