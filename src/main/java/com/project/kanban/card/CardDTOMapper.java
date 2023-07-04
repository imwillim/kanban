package com.project.kanban.card;

import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CardDTOMapper implements Function<Card, CardDTO> {
    @Override
    public CardDTO apply(Card card){
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.isArchived(),
                card.getColumnOrder(),
                card.getRowOrder(),
                card.getUpdatedAt(),
                card.getCreatedAt(),
                card.getListing().getId()
        );
    }
}
