package com.gopisvdev.TalkSync.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "message_seen")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageSeen {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private OffsetDateTime seenAt;
}
