package com.project.kanban.listing;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.board.Board;
import com.project.kanban.board.BoardException;
import com.project.kanban.board.BoardService;
import com.project.kanban.user.UserException;
import com.project.kanban.userboard.UserBoardService;
import com.project.kanban.userlisting.UserListingService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ListingFacadeServiceImpl implements ListingFacadeService {
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final ListingService listingService;

    private final UserBoardService userBoardService;

    private final UserListingService userListingService;
    private final ListingDTOMapper listingDTOMapper;

    public ListingFacadeServiceImpl(WorkspaceService workspaceService,
                                    BoardService boardService,
                                    ListingService listingService,
                                    UserBoardService userBoardService,
                                    UserListingService userListingService,
                                    ListingDTOMapper listingDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.userBoardService = userBoardService;
        this.userListingService = userListingService;
        this.listingDTOMapper = listingDTOMapper;
    }

    @Override
    public List<ListingDTO> getListingsProcess(long workspaceId,
                                               long boardId){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        Optional<Board> board = Optional.of(boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new));
        return board
                .map(value -> listingService
                        .getListings(value)
                        .stream()
                        .filter(listing -> !listing.isArchived())
                        .map(listingDTOMapper)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<ListingDTO> getListingProcess(Authentication authentication, long workspaceId,
                                                  long boardId, long listingId){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(workspaceId, boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        return listingService.getListing(boardId, listingId).map(listingDTOMapper);
    }

    @Override
    public Optional<ListingDTO> createListingProcess(Authentication authentication,
                                                     long workspaceId, long boardId,
                                                     ListingDTO listingDTO){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        Optional<Board> board = Optional.of(boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new));


        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userBoardRole = userBoardService.getRoleFromUserBoard(userId, board.get().getId());
        if (!AuthService.checkAuthority(userBoardRole, "CREATOR"))
            throw new AuthException.ForbiddenError();

        Board existedBoard = board.get();
        Optional<Listing> listing =  Optional.of(listingService
                .createListing(listingDTO, existedBoard));
        existedBoard.getListings().add(listing.get());

        // Add more UserListing with Role
        userListingService.createUserListing(userId, listing.get().getId(), "CREATOR");
        return listing.map(listingDTOMapper);

    }

    @Override
    public Optional<ListingDTO> updateListingProcess(Authentication authentication,
                                                     long workspaceId, long boardId,
                                         long listingId, ListingDTO listingDTO){

        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        Optional<Listing> updatedListing = Optional.of(listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new));

        return Optional.of(listingService.updateListing(updatedListing.get(), listingDTO))
                .map(listingDTOMapper);
    }

    @Override
    public void deleteListingProcess(Authentication authentication, long workspaceId, long boardId, long listingId){
        Optional.ofNullable(boardService.getBoard(workspaceId, boardId))
                .orElseThrow(BoardException.BoardNotFound::new);

        Optional<Listing> listing = Optional.of(listingService.getListing(boardId, listingId)
                .orElseThrow(ListingException.ListingNotFound::new));

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userListingRole = userListingService.getRoleFromUserListing(userId, listing.get().getId());

        if (!AuthService.checkAuthority(userListingRole, "CREATOR"))
            throw new AuthException.ForbiddenError();

        listingService.deleteListing(listingId);
    }

    @Override
    public void assignListing(Authentication authentication,long boardId, long listingId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userBoardService.getUserBoardByUserAndBoardIds(userId, boardId)
                .orElseThrow(AuthException.ForbiddenError::new);

        if(userListingService.getUserListingByUserAndListingIds(userId, listingId).isPresent())
            throw new UserException.UserNotValidParams();

        userListingService.createUserListing(userId, listingId, "MEMBER");
    }

    // Add more Authority function with Role, Authentication
    // Add more Assign function with Role
}
