package com.project.kanban.card;

import com.project.kanban.listing.Listing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Optional<Card> getCard(long cardId) {
        return Optional.empty();
    }

    @Override
    public Card createCard(CardDTO cardDTO, Listing listing) {
        Card createdCard = new Card(cardDTO.getTitle(), cardDTO.getDescription(), listing);
        return saveCard(createdCard);
    }

    @Override
    public Card updateCard(long cardId, CardDTO cardDTO) {
        Card card = cardRepository.findById(cardId).orElse(null);
        if (card != null){
            card.setTitle(cardDTO.getTitle());
            card.setDescription(cardDTO.getDescription());
            card.setArchived(cardDTO.isArchived());
            card.setUpdatedAt(LocalDateTime.now());
        }
        return null;
    }

    @Override
    public void deleteCard(long cardId) {
         cardRepository.deleteById(cardId);
    }

    @Override
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
}
