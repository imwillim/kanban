package com.project.kanban.card;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.board.Board;
import com.project.kanban.board.BoardException;
import com.project.kanban.board.BoardService;
import com.project.kanban.listing.Listing;
import com.project.kanban.listing.ListingException;
import com.project.kanban.listing.ListingService;
import com.project.kanban.user.User;
import com.project.kanban.user.UserException;
import com.project.kanban.user.UserService;
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
    private final UserService userService;
    private final UserListingService userListingService;
    private final UserCardService userCardService;
    private final CardDTOMapper cardDTOMapper;

    public CardFacadeServiceImpl(WorkspaceService workspaceService, BoardService boardService,
                                 ListingService listingService, CardService cardService,
                                 UserService userService, UserListingService userListingService,
                                 UserCardService userCardService, CardDTOMapper cardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.cardService = cardService;
        this.userService = userService;
        this.userListingService = userListingService;
        this.userCardService = userCardService;
        this.cardDTOMapper = cardDTOMapper;
    }

    @Override
    public List<CardDTO> getCardsProcess(Authentication authentication, long workspaceId,
                                         long boardId, long listingId) {

        validateIds(workspaceId, boardId, listingId);
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        if (userListingService.getUserListingByUserAndListingIds(userId, listingId).isEmpty())
            throw new AuthException.ForbiddenError();

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
        validateIds(workspaceId, boardId, listingId);
        return cardService.getCardByListingAndCardIds(listingId, cardId).map(cardDTOMapper);
    }

    @Override
    public Optional<CardDTO> createCardProcess(Authentication authentication,
                                               long workspaceId, long boardId,
                                               long listingId, CardDTO cardDTO) {
        validateIds(workspaceId, boardId, listingId);

        Optional<Listing> listing = listingService.getListing(boardId, listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();

        Listing existedListing = listing.get();
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        checkCreatorList(userId, existedListing.getId());

        Optional<Card> createdCard = Optional.of(cardService.createCard(cardDTO, listing.get()));
        existedListing.getCards().add(createdCard.get());
        userCardService.createUserCard(userId, createdCard.get().getId(), "CREATOR");
        return createdCard.map(cardDTOMapper);

    }

    @Override
    public Optional<CardDTO> updateCardProcess(Authentication authentication,
                                               long workspaceId, long boardId, long listingId,
                                               long cardId, CardDTO requestBody) {
        validateIds(workspaceId, boardId, listingId);
        Optional<Card> updatedCard = Optional
                .of(cardService.getCardByListingAndCardIds(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));

        return Optional.of(cardService.updateCard(updatedCard.get(), requestBody))
                .map(cardDTOMapper);
    }

    @Override
    public void deleteCardProcess(Authentication authentication, long workspaceId, long boardId,
                                  long listingId, long cardId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        validateIds(workspaceId, boardId, listingId);

        Optional<Card> card = Optional
                .of(cardService.getCardByListingAndCardIds(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));

        checkCreatorCard(userId, card.get().getId());
        cardService.deleteCard(cardId);
    }

    @Override
    public Optional<CardDTO> modifyArchiveCard(Authentication authentication, long workspaceId, long boardId,
                                               long listingId, long cardId, CardDTO requestBody) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        validateIds(workspaceId, boardId, listingId);
        Optional<Card> card = Optional
                .of(cardService.getCardByListingAndCardIds(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));

        checkCreatorCard(userId, card.get().getId());
        return Optional.of(cardService.modifyArchive(card.get(), requestBody))
                .map(cardDTOMapper);
    }

    @Override
    public void assignCard(Authentication authentication, long listingId,
                           long cardId, CardAssignRequest cardAssignRequest) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userListingService.getUserListingByUserAndListingIds(userId, listingId)
                .orElseThrow(AuthException.ForbiddenError::new);

        checkCreatorCard(userId, cardId);

        Optional<User> assignedUser = Optional.of(userService.getUserByEmail(cardAssignRequest.getEmail())
                .orElseThrow(UserException.UserNotFound::new));
        long assignedUserId = assignedUser.get().getId();

        if (userCardService.getUserCardByUserAndCardIds(assignedUserId, cardId).isPresent())
            throw new UserException.UserNotValidParams();

        userCardService.createUserCard(assignedUserId, cardId, "MEMBER");

    }

    @Override
    public Optional<CardDTO> dragCard(Authentication authentication, long workspaceId, long boardId,
                                      long listingId, long cardId, CardDTO cardDTO) {
        Optional<Listing> listing = listingService.getListing(boardId, listingId);
        if (listing.isEmpty())
            throw new ListingException.ListingNotFound();


        Optional<Card> card = Optional
                .of(cardService.getCardByListingAndCardIds(listingId, cardId)
                .orElseThrow(CardException.CardNotFound::new));


        long userId = AuthService.getUserIdFromAuthentication(authentication);
        checkCreatorCard(userId, cardId);

        setLimitColumnOrderCard(workspaceId, boardId, cardDTO);
        setLimitRowOrderCard(boardId, listingId, cardDTO);
        changeColumnOrderCards(boardId, listingId, cardDTO);

        return Optional.of(
                cardService.modifyOrders(card.get(), cardDTO.getColumnOrder(), cardDTO.getRowOrder()))
                .map(cardDTOMapper);
    }

    private void validateIds(long workspaceId, long boardId, long listingId) {
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new);

        listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new);
    }

    private void checkCreatorList(long userId, long listingId){
        String userListingRole = userListingService.getRoleFromUserListing(userId, listingId);
        if (!AuthService.checkAuthority(userListingRole, "CREATOR"))
            throw new AuthException.ForbiddenError();

    }


    private void checkCreatorCard(long userId, long cardId){
        String userCardRole = userCardService.getRoleFromUserCard(userId, cardId);
        if (!AuthService.checkAuthority(userCardRole, "CREATOR"))
            throw new AuthException.ForbiddenError();
    }

    private void setLimitColumnOrderCard(long workspaceId, long boardId, CardDTO cardDTO){
        Board board = boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new);

        int maxColumnOrder = board.getListings().size() - 1;
        if (cardDTO.getColumnOrder() > maxColumnOrder)
            cardDTO.setColumnOrder(maxColumnOrder);
        else if (cardDTO.getColumnOrder() < 0)
            cardDTO.setColumnOrder(0);
    }


    private void setLimitRowOrderCard(long boardId, long listingId, CardDTO cardDTO){
        Listing listing = listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new);

        int maxRowOrder = listing.getCards().size() - 1;
        if (cardDTO.getRowOrder() > maxRowOrder){
            cardDTO.setRowOrder(maxRowOrder);
        }
        else if (cardDTO.getRowOrder() < 0){
            cardDTO.setRowOrder(0);
        }
    }


    private void changeColumnOrderCards(long boardId, long listingId, CardDTO cardDTO){
        Listing listing = listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new);

        List<Card> cards = cardService.getCards(listing);

        int start = cardDTO.getRowOrder();
        for (int i = start; i < cards.size(); i++){
            int oldColumnOrder = cards.get(i).getColumnOrder();
            cards.get(i).setColumnOrder(oldColumnOrder + 1);
        }
    }
}


