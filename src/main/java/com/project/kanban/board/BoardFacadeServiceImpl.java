package com.project.kanban.board;

import com.project.kanban.listing.ListingDTO;
import com.project.kanban.listing.Listing;
import com.project.kanban.workspace.Workspace;
import com.project.kanban.listing.ListingService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BoardFacadeServiceImpl implements BoardFacadeService {
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final ListingService listingService;
    private final BoardDTOMapper boardDTOMapper;

    public BoardFacadeServiceImpl(WorkspaceService workspaceService,
                                  BoardService boardService,
                                  ListingService listingService,
                                  BoardDTOMapper boardDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.boardDTOMapper = boardDTOMapper;
    }


    @Override
    public List<BoardDTO> getBoardsProcess(long workspaceId) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);
        return workspace
                .map(value -> boardService
                        .getBoards(value)
                        .stream()
                        .map(boardDTOMapper)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<BoardDTO> getBoardProcess(long workspaceId, long boardId) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);
        return workspace.isPresent() ? boardService.getBoard(boardId).map(boardDTOMapper) : Optional.empty();
    }


    @Override
    public Optional<BoardDTO> createBoardProcess(long workspaceId, BoardDTO requestBody){
        Optional<Workspace> workspace = Optional.ofNullable(workspaceService.getWorkspace(workspaceId)
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new));
        if (workspace.isPresent()) {
            Board createdBoard = boardService.createBoard(requestBody, workspace.get());
            Listing defaultListing = listingService.createListing(new ListingDTO("Default"), createdBoard);
            createdBoard.getListings().add(defaultListing);
            return Optional.of(boardService.saveBoard(createdBoard)).map(boardDTOMapper);
        }
        return Optional.empty();

    }


    @Override
    public Optional<BoardDTO> updateBoardProcess(long workspaceId, long boardId, BoardDTO requestBody){
        Board updatedBoard = boardService.updateBoard(boardId, requestBody);
        return Optional.of(updatedBoard).map(boardDTOMapper);
    }


    @Override
    public void deleteBoardProcess(long workspaceId, long boardId) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);
        if (workspace.isEmpty())
            throw new BoardException.BoardNotFound();
        boardService.deleteBoard(boardId);
    }
}
