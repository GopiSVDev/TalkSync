package com.gopisvdev.TalkSync.repository;

import com.gopisvdev.TalkSync.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String partialUsername);

    boolean existsByUsername(String username);

    List<User> findByIsOnlineTrue();

    List<User> findByIsTemporaryTrueAndExpiresAtBefore(LocalDateTime time);
}
