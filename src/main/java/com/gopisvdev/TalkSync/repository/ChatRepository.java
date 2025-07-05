package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("""
              SELECT c FROM Chat c
              JOIN c.participants p
              WHERE p.user.id = :userId
              ORDER BY c.createdAt DESC
            """)
    List<Chat> findAllByUserId(UUID userId);

    @Query("""
                SELECT c FROM Chat c
                JOIN c.participants p1
                JOIN c.participants p2
                WHERE c.isGroup = false
                AND p1.user.id = :userA AND p2.user.id = :userB
            """)
    Optional<Chat> findDirectChatBetween(UUID userA, UUID userB);

    @Query("""
              SELECT c FROM Chat c
              LEFT JOIN FETCH c.participants p
              LEFT JOIN FETCH p.user
              WHERE c.id = :chatId
            """)
    Optional<Chat> findByIdWithParticipants(UUID chatId);

}
