package com.project.kanban.card;

import com.project.kanban.listing.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    @Autowired
    public CardServiceImpl(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    @Override
    public List<Card> getCards(Listing listing) {
        return cardRepository.findAll();
    }

    @Override
    public Optional<Card> getCard(long listingId, long cardId) {
        return cardRepository.getCardByListingIdAndId(listingId, cardId);
    }

    @Override
    public Card createCard(CardDTO cardDTO, Listing listing) {
        Card createdCard = new Card(cardDTO.getTitle(), cardDTO.getDescription(), listing);
        return cardRepository.save(createdCard);
    }

    @Override
    public Card updateCard(Card card, CardDTO cardDTO) {
        if (card != null){
            card.setTitle(cardDTO.getTitle());
            card.setDescription(cardDTO.getDescription());
            card.setArchived(cardDTO.isArchived());
            card.setUpdatedAt(Timestamp.from(Instant.now()).getTime());
            return cardRepository.save(card);
        }
        return null;
    }

    @Override
    public void deleteCard(long cardId) {
        cardRepository.deleteById(cardId);
    }

}
