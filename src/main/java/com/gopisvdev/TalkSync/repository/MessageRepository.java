package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByChatIdOrderByCreatedAtAsc(UUID chatId);

    Page<Message> findByChatIdOrderByCreatedAtDesc(UUID chatId, Pageable pageable);

    @Query("""
                SELECT m FROM Message m
                WHERE m.chat.id = :chatId
                ORDER BY m.createdAt DESC
                LIMIT 1
            """)
    Optional<Message> findLastMessageByChatId(UUID chatId);

    List<Message> findByChatIdAndContentContainingIgnoreCase(UUID chatId, String keyword);

    List<Message> findBySenderId(UUID userId);

    void deleteAllBySender_Id(UUID userId);
}
