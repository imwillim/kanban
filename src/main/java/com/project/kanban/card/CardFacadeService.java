package com.project.kanban.card;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
public interface CardFacadeService {
    List<CardDTO> getCardsProcess(long workspaceId, long boardId, long listingId);
    Optional<CardDTO> getCardProcess(Authentication authentication, long workspaceId, long boardId,
                                     long listingId, long cardId);
    Optional<CardDTO> createCardProcess(Authentication authentication, long workspaceId, long boardId,
                                        long listingId, CardDTO cardDTO);
    Optional<CardDTO> updateCardProcess(Authentication authentication, long workspaceId, long boardId,
                                        long listingId, long cardId, CardDTO cardDTO);
    void deleteCardProcess(Authentication authentication, long workspaceId, long boardId,
                           long listingId, long cardId);

    void assignCard(Authentication authentication, long listingId, long cardId);


}
