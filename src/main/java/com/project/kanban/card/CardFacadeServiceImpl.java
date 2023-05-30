package com.project.kanban.card;

import com.project.kanban.board.BoardException;
import com.project.kanban.board.BoardService;
import com.project.kanban.listing.Listing;
import com.project.kanban.listing.ListingException;
import com.project.kanban.listing.ListingService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CardFacadeServiceImpl implements CardFacadeService {
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final ListingService listingService;
    private final CardService cardService;
    private final CardDTOMapper cardDTOMapper;

    public CardFacadeServiceImpl(WorkspaceService workspaceService, BoardService boardService,
                                 ListingService listingService, CardService cardService,
                                 CardDTOMapper cardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.cardService = cardService;
        this.cardDTOMapper = cardDTOMapper;
    }

    @Override
    public List<CardDTO> getCardsProcess(long workspaceId, long boardId,
                                         long listingId) {

        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        Optional<Listing> listing = listingService.getListing(listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();
        return listing
                .map(value -> cardService
                        .getCards(value)
                        .stream()
                        .map(cardDTOMapper)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<CardDTO> getCardProcess(long workspaceId, long boardId,
                                            long listingId, long cardId) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        if (listingService.getListing(listingId).isEmpty())
            throw new ListingException.ListingNotFound();

        return cardService.getCard(cardId).map(cardDTOMapper);
    }

    @Override
    public Optional<CardDTO> createCardProcess(long workspaceId, long boardId,
                                               long listingId, CardDTO cardDTO) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();


        Optional<Listing> listing = listingService.getListing(listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();
        else
            return Optional.of(cardService.createCard(cardDTO, listing.get())).map(cardDTOMapper);
    }

    @Override
    public Optional<CardDTO> updateCardProcess(long workspaceId, long boardId, long listingId,
                                               long cardId, CardDTO requestBody) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        if (listingService.getListing(listingId).isEmpty())
            throw new ListingException.ListingNotFound();

        Card updatedCard = cardService.updateCard(cardId, requestBody);
        return Optional.of(updatedCard).map(cardDTOMapper);
    }

    @Override
    public void deleteCardProcess(long workspaceId, long boardId,
                                  long listingId, long cardId) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        if (listingService.getListing(listingId).isEmpty())
            throw new ListingException.ListingNotFound();

        cardService.deleteCard(cardId);
    }
}
