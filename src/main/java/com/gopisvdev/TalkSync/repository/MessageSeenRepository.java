package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.MessageSeen;
import com.gopisvdev.TalkSync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageSeenRepository extends JpaRepository<MessageSeen, UUID> {
    List<MessageSeen> findByMessageId(UUID messageId);

    boolean existsByMessageIdAndUserId(UUID messageId, UUID userId);

    @Query("""
                SELECT ms FROM MessageSeen ms
                WHERE ms.user.id = :userId AND ms.message.chat.id = :chatId
            """)
    List<MessageSeen> findByUserIdAndChatId(UUID userId, UUID chatId);

    @Query("""
                SELECT ms.user FROM MessageSeen ms
                WHERE ms.message.id = :messageId
            """)
    List<User> findSeenUsersByMessageId(UUID messageId);

    @Modifying
    void deleteAllByUserId(UUID userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MessageSeen ms WHERE ms.message.chat.id = :chatId")
    void deleteByChatId(@Param("chatId") UUID chatId);
}
