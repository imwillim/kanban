package com.project.kanban.board;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.listing.ListingDTO;
import com.project.kanban.listing.Listing;
import com.project.kanban.user.UserException;
import com.project.kanban.userboard.UserBoardService;
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
    private final UserBoardService userBoardService;
    private final UserWorkspaceService userWorkspaceService;
    private final BoardDTOMapper boardDTOMapper;

    @Autowired
    public BoardFacadeServiceImpl(WorkspaceService workspaceService,
                                  BoardService boardService,
                                  ListingService listingService,
                                  UserWorkspaceService userWorkspaceService,
                                  UserBoardService userBoardService,
                                  BoardDTOMapper boardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.userWorkspaceService = userWorkspaceService;
        this.userBoardService = userBoardService;
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
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspace.get().getId());

        if (!AuthService.checkAuthority(userWorkspaceRole, "OWNER") &&
                !AuthService.checkAuthority(userWorkspaceRole, "ADMIN"))
            throw new AuthException.ForbiddenError();

        Board createdBoard = boardService.createBoard(requestBody, workspace.get());
        Listing defaultListing = listingService.createListing(new ListingDTO("Default"), createdBoard);
        createdBoard.getListings().add(defaultListing);

        userBoardService.createUserBoard(userId, createdBoard.getId(), "CREATOR");
        return Optional.of(boardService.saveBoard(createdBoard)).map(boardDTOMapper);
    }

    @Override
    public Optional<BoardDTO> updateBoardProcess(Authentication authentication, long workspaceId, long boardId, BoardDTO requestBody){
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        Optional<Board> board = Optional.ofNullable(boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new));

        return Optional.of(boardService.updateBoard(board.get(), requestBody))
                .map(boardDTOMapper);
    }


    @Override
    public void deleteBoardProcess(Authentication authentication, long workspaceId, long boardId) {
        workspaceService.getWorkspace(workspaceId)
                .orElseThrow(BoardException.BoardNotFound::new);

        boardService.getBoard(workspaceId, boardId)
                .orElseThrow(BoardException.BoardNotFound::new);

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userBoardRole = userBoardService.getRoleFromUserBoard(userId, boardId);

        if (!AuthService.checkAuthority(userBoardRole, "CREATOR"))
            throw new AuthException.ForbiddenError();

        boardService.deleteBoard(boardId);
    }

    @Override
    public void assignBoard(Authentication authentication, long workspaceId, long boardId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userWorkspaceService.getUserWorkspaceByUserAndWorkspaceIds(userId, boardId)
                .orElseThrow(AuthException.ForbiddenError::new);

        if (userBoardService.getUserBoardByUserAndBoardIds(userId, boardId).isPresent())
            throw new UserException.UserNotValidParams();

        userBoardService.createUserBoard(userId, boardId, "MEMBER");
    }


    // Add more Authority function with Role, Authentication
    // Add more Assign function with Role
}
