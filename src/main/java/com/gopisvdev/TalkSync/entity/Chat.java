package com.gopisvdev.TalkSync.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    @CreationTimestamp
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ChatParticipant> participants = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Message lastMessage;
}
