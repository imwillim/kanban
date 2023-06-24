package com.project.kanban.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findById(long cardId);

    Optional<Card> getCardByListingIdAndId(long listingId, long cardId);
}
