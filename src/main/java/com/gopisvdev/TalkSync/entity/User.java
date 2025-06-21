package com.gopisvdev.TalkSync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @Column(unique = true, nullable = false)
    private String username;
    private String passwordHash;
    private String avatarUrl;

    @Column(nullable = true)
    private String email;
    private Boolean isOnline;
    private LocalDateTime lastSeen;

    private Boolean isTemporary = false;
    private LocalDateTime expiresAt;
}
