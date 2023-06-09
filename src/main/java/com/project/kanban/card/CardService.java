package com.project.kanban.card;

import com.project.kanban.listing.Listing;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<Card> getCards(Listing listing);

    Optional<Card> getCard(long listingId, long cardId);

    Card createCard(CardDTO requestBody, Listing listing);

    Card updateCard(Card card, CardDTO cardDTO);

    void deleteCard(long cardId);
}
