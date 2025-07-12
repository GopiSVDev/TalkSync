package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    
    @Query("SELECT DISTINCT c FROM Chat c " +
            "LEFT JOIN FETCH c.participants cp " +
            "LEFT JOIN FETCH cp.user " +
            "LEFT JOIN FETCH c.lastMessage " +
            "WHERE EXISTS (" +
            "   SELECT 1 FROM ChatParticipant cp2 WHERE cp2.chat = c AND cp2.user.id = :userId" +
            ")")
    List<Chat> findAllByUserId(UUID userId);

    @Query("""
                SELECT c FROM Chat c
                JOIN c.participants p
                WHERE c.isGroup = false
                  AND p.user.id IN (:userA, :userB)
                GROUP BY c
                HAVING COUNT(DISTINCT p.user.id) = 2
            """)
    Optional<Chat> findDirectChatBetween(@Param("userA") UUID userA, @Param("userB") UUID userB);

    @Query("""
              SELECT c FROM Chat c
              LEFT JOIN FETCH c.participants p
              LEFT JOIN FETCH p.user
              WHERE c.id = :chatId
            """)
    Optional<Chat> findByIdWithParticipants(UUID chatId);

    @Modifying
    @Transactional
    @Query("UPDATE Chat c SET c.lastMessage = NULL WHERE c.id = :chatId")
    void clearLastMessage(@Param("chatId") UUID chatId);
}
