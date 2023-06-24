package com.project.kanban.usercard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    Optional<UserCard> findUserCardByUserIdAndCardId(long userId, long cardId);
}
