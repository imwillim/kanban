package com.project.kanban.card;

import java.util.List;
import java.util.Optional;

public interface CardFacadeService {
    List<CardDTO> getCardsProcess(long workspaceId, long boardId, long listingId);
    Optional<CardDTO> getCardProcess(long workspaceId, long boardId, long listingId, long cardId);
    Optional<CardDTO> createCardProcess(long workspaceId, long boardId, long listingId, CardDTO cardDTO);
    Optional<CardDTO> updateCardProcess(long workspaceId, long boardId, long listingId, long cardId, CardDTO cardDTO);
    void deleteCardProcess(long workspaceId, long boardId, long listingId, long cardId);

}
