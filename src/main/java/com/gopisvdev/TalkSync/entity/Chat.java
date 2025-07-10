package com.gopisvdev.TalkSync.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String avatarUrl;
    private Boolean isGroup = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    private List<ChatParticipant> participants = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Message lastMessage;
}
