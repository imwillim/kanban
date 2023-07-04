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
    public Optional<Card> getCardByListingAndCardIds(long listingId, long cardId) {
        return cardRepository.getCardByListingIdAndId(listingId, cardId);
    }

    @Override
    public Card createCard(CardDTO cardDTO, Listing listing) {
        Card createdCard = new Card(cardDTO.getTitle(), cardDTO.getDescription(),
                listing.getColumnOrder(), listing.getCards().size(), listing);
        return cardRepository.save(createdCard);
    }

    @Override
    public Card updateCard(Card updatedCard, CardDTO cardDTO) {
        if (updatedCard != null){
            updatedCard.setTitle(cardDTO.getTitle());
            updatedCard.setDescription(cardDTO.getDescription());
            updatedCard.setArchived(cardDTO.isArchived());
            updatedCard.setUpdatedAt(Timestamp.from(Instant.now()).getTime());
            return cardRepository.save(updatedCard);
        }
        return null;
    }

    @Override
    public void deleteCard(long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Card modifyArchive(Card updatedCard, CardDTO cardDTO) {
        if(updatedCard != null) {
            updatedCard.setArchived(cardDTO.isArchived());
            return cardRepository.save(updatedCard);
        }
        return null;
    }

    @Override
    public Card modifyOrders(Card updatedCard, int columnOrder, int rowOrder) {
        if (updatedCard != null) {
            updatedCard.setColumnOrder(columnOrder);
            updatedCard.setRowOrder(rowOrder);
            return cardRepository.save(updatedCard);
        }
        return null;
    }

}
