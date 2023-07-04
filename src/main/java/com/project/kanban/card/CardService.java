package com.project.kanban.card;

import com.project.kanban.listing.Listing;

import java.util.List;
import java.util.Optional;

public interface CardService {
    List<Card> getCards(Listing listing);

    Optional<Card> getCardByListingAndCardIds(long listingId, long cardId);

    Card createCard(CardDTO requestBody, Listing listing);

    Card updateCard(Card card, CardDTO cardDTO);

    void deleteCard(long cardId);

    Card modifyArchive(Card card, CardDTO cardDTO);

    Card modifyOrders(Card card, int columnOrder, int rowOrder);

}
