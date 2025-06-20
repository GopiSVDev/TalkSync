package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.ChatParticipant;
import com.gopisvdev.TalkSync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, UUID> {

    List<ChatParticipant> findByChatId(UUID chatId);

    boolean existsByChatIdAndUserId(UUID chatId, UUID userId);

    Optional<ChatParticipant> findByChatIdAndUserId(UUID chatId, UUID userId);

    List<ChatParticipant> findByUserId(UUID userId);

    @Query("""
                SELECT cp.user FROM ChatParticipant cp
                WHERE cp.chat.id = :chatId AND cp.user.id <> :userId
            """)
    Optional<User> findOtherParticipant(UUID chatId, UUID userId);
}
