package com.project.kanban.board;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.listing.ListingDTO;
import com.project.kanban.listing.Listing;
import com.project.kanban.user.User;
import com.project.kanban.user.UserException;
import com.project.kanban.user.UserService;
import com.project.kanban.userboard.UserBoardService;
import com.project.kanban.userlisting.UserListingService;
import com.project.kanban.userworkspace.UserWorkspaceService;
import com.project.kanban.workspace.Workspace;
import com.project.kanban.listing.ListingService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BoardFacadeServiceImpl implements BoardFacadeService {
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final ListingService listingService;
    private final UserService userService;
    private final UserWorkspaceService userWorkspaceService;
    private final UserBoardService userBoardService;
    private final UserListingService userListingService;
    private final BoardDTOMapper boardDTOMapper;

    @Autowired
    public BoardFacadeServiceImpl(WorkspaceService workspaceService,
                                  BoardService boardService,
                                  ListingService listingService,
                                  UserService userService,
                                  UserWorkspaceService userWorkspaceService,
                                  UserBoardService userBoardService,
                                  UserListingService userListingService,
                                  BoardDTOMapper boardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.userService = userService;
        this.userWorkspaceService = userWorkspaceService;
        this.userBoardService = userBoardService;
        this.userListingService = userListingService;
        this.boardDTOMapper = boardDTOMapper;
    }


    @Override
    public List<BoardDTO> getBoardsProcess(Authentication authentication, long workspaceId) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);
        if (workspace.isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        long userId = AuthService.getUserIdFromAuthentication(authentication);

        userWorkspaceService.getUserWorkspaceByUserAndWorkspaceIds(userId, workspaceId)
                .orElseThrow(AuthException.ForbiddenError::new);

        return workspace
                .map(value -> boardService
                        .getBoards(value)
                        .stream()
                        .map(boardDTOMapper)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<BoardDTO> getBoardProcess(Authentication authentication, long workspaceId, long boardId) {
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);
        return boardService.getBoard(workspaceId, boardId).map(boardDTOMapper);
    }


    @Override
    public Optional<BoardDTO> createBoardProcess(Authentication authentication,
                                                 long workspaceId, BoardDTO requestBody){
        Optional<Workspace> workspace = Optional.of(workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new));

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        checkCreatorAdminWorkspace(userId, workspaceId);

        Board createdBoard = createDefaultBoard(authentication, requestBody, workspace.get());
        userBoardService.createUserBoard(userId, createdBoard.getId(), "CREATOR");
        return Optional.of(boardService.saveBoard(createdBoard)).map(boardDTOMapper);
    }

    @Override
    public Optional<BoardDTO> updateBoardProcess(Authentication authentication, long workspaceId,
                                                 long boardId, BoardDTO requestBody){
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        Optional<Board> board = Optional.of(boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new));

        return Optional.of(boardService.updateBoard(board.get(), requestBody))
                .map(boardDTOMapper);
    }


    @Override
    public void deleteBoardProcess(Authentication authentication, long workspaceId, long boardId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(BoardException.BoardNotFound::new);

        boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new);

        checkCreatorBoard(userId, boardId);
        boardService.deleteBoard(boardId);
    }

    @Override
    public void assignBoard(Authentication authentication, long workspaceId, long boardId,
                            BoardAssignRequest boardAssignRequest) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userWorkspaceService.getUserWorkspaceByUserAndWorkspaceIds(userId, boardId)
                .orElseThrow(AuthException.ForbiddenError::new);

        checkCreatorBoard(userId, workspaceId);

        Optional<User> assignedUser = Optional.of(userService.getUserByEmail(boardAssignRequest.email)
                .orElseThrow(UserException.UserNotFound::new));
        long assignedUserId = assignedUser.get().getId();
        if (userBoardService.getUserBoardByUserAndBoardIds(assignedUserId, boardId).isPresent())
            throw new UserException.UserNotValidParams();

        userBoardService.createUserBoard(assignedUserId, boardId, "MEMBER");
    }


    private void checkCreatorBoard(long userId, long boardId){
        String userBoardRole = userBoardService.getRoleFromUserBoard(userId, boardId);
        if (!AuthService.checkAuthority(userBoardRole, "CREATOR"))
            throw new AuthException.ForbiddenError();
    }


    private void checkCreatorAdminWorkspace(long userId, long workspaceId){
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspaceId);
        System.out.println(userWorkspaceRole);
        if (!AuthService.checkAuthority(userWorkspaceRole, "OWNER") &&
                !AuthService.checkAuthority(userWorkspaceRole, "ADMIN"))
            throw new AuthException.ForbiddenError();
    }

    private Board createDefaultBoard(Authentication authentication, BoardDTO requestBody, Workspace workspace) {
        Board createdBoard = boardService.createBoard(requestBody, workspace);
        long userId = AuthService.getUserIdFromAuthentication(authentication);

        String []titles = {"Doing", "Done", "To-do"};
        for (int i = 0; i < 3; i++){
            createDefaultListing(userId, createdBoard, titles[i]);
        }
        return createdBoard;
    }

    private void createDefaultListing(long userId, Board createdBoard, String title){
        Listing defaultListing = listingService.createListing(
                new ListingDTO(title, createdBoard.getListings().size() + 1), createdBoard);
        createdBoard.getListings().add(defaultListing);
        userListingService.createUserListing(userId, defaultListing.getId(), "CREATOR");
    }
}
