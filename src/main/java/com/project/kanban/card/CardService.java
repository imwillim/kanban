package com.project.kanban.card;

import com.project.kanban.listing.Listing;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<Card> getCards(Listing listing);

    Optional<Card> getCard(long cardId);

    Card createCard(CardDTO requestBody, Listing listing);

    Card updateCard(long cardId, CardDTO cardDTO);

    void deleteCard(long cardId);

    Card saveCard(Card card);
}
