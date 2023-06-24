package com.project.kanban.card;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.board.BoardException;
import com.project.kanban.board.BoardService;
import com.project.kanban.listing.Listing;
import com.project.kanban.listing.ListingException;
import com.project.kanban.listing.ListingService;
import com.project.kanban.user.UserException;
import com.project.kanban.usercard.UserCardService;
import com.project.kanban.userlisting.UserListingService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.security.core.Authentication;
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
    private final UserListingService userListingService;
    private final UserCardService userCardService;
    private final CardDTOMapper cardDTOMapper;

    public CardFacadeServiceImpl(WorkspaceService workspaceService, BoardService boardService,
                                 ListingService listingService, CardService cardService,
                                 UserListingService userListingService, UserCardService userCardService,
                                 CardDTOMapper cardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.cardService = cardService;
        this.userListingService = userListingService;
        this.userCardService = userCardService;
        this.cardDTOMapper = cardDTOMapper;
    }

    @Override
    public List<CardDTO> getCardsProcess(long workspaceId, long boardId,
                                         long listingId) {

        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(workspaceId, boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        Optional<Listing> listing = listingService.getListing(boardId, listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();
        return listing
                .map(value -> cardService
                        .getCards(value)
                        .stream()
                        .filter(card -> !card.isArchived())
                        .map(cardDTOMapper)
                        .toList())
                .orElse(Collections.emptyList());

        // Add more UserCard with Role

    }

    @Override
    public Optional<CardDTO> getCardProcess(Authentication authentication,
                                            long workspaceId, long boardId,
                                            long listingId, long cardId) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(workspaceId, boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        if (listingService.getListing(boardId, listingId).isEmpty())
            throw new ListingException.ListingNotFound();

        return cardService.getCard(listingId, cardId).map(cardDTOMapper);
    }

    @Override
    public Optional<CardDTO> createCardProcess(Authentication authentication,
                                               long workspaceId, long boardId,
                                               long listingId, CardDTO cardDTO) {

        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(workspaceId, boardId).isEmpty())
            throw new BoardException.BoardNotFound();


        Optional<Listing> listing = listingService.getListing(boardId, listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();

        Listing existedListing = listing.get();

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userListingRole = userListingService.getRoleFromUserListing(userId, listing.get().getId());
        if (!AuthService.checkAuthority(userListingRole, "CREATOR"))
            throw new AuthException.ForbiddenError();


        Optional<Card> createdCard = Optional.of(cardService.createCard(cardDTO, listing.get()));
        existedListing.getCards().add(createdCard.get());
        userCardService.createUserCard(userId, createdCard.get().getId(), "CREATOR");
        return createdCard.map(cardDTOMapper);

    }

    @Override
    public Optional<CardDTO> updateCardProcess(Authentication authentication,
                                               long workspaceId, long boardId, long listingId,
                                               long cardId, CardDTO requestBody) {
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(workspaceId, boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        if (listingService.getListing(boardId, listingId).isEmpty())
            throw new ListingException.ListingNotFound();

        Optional<Card> updatedCard = Optional.of(cardService.getCard(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));

        return Optional.of(cardService.updateCard(updatedCard.get(), requestBody))
                .map(cardDTOMapper);
    }

    @Override
    public void deleteCardProcess(Authentication authentication, long workspaceId, long boardId,
                                  long listingId, long cardId) {
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new);

        listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new);

        Optional<Card> card = Optional.of(cardService.getCard(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userListingRole = userCardService.getRoleFromUserCard(userId, card.get().getId());
        if (!AuthService.checkAuthority(userListingRole, "CREATOR"))
            throw new AuthException.ForbiddenError();

        cardService.deleteCard(cardId);
    }

    @Override
    public void assignCard(Authentication authentication, long listingId, long cardId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userListingService.getUserListingByUserAndListingIds(userId, listingId)
                .orElseThrow(AuthException.ForbiddenError::new);

        if (userCardService.getUserCardByUserAndCardIds(userId, cardId).isPresent())
            throw new UserException.UserNotValidParams();

        userCardService.createUserCard(userId, cardId, "MEMBER");


    }
}


    // Add more Authority function with Role, Authentication
    // Add more Assign function with Role

