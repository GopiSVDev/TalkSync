package com.gopisvdev.TalkSync.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRegisterRequest {

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only letters and numbers")
    private String username;

    @NotBlank(message = "Name is required")
    private String displayName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
