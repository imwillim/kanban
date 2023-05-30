package com.project.kanban.listing;

import com.project.kanban.board.Board;
import com.project.kanban.board.BoardException;
import com.project.kanban.board.BoardService;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.workspace.WorkspaceService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ListingFacadeServiceImpl implements ListingFacadeService {
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final ListingService listingService;
    private final ListingDTOMapper listingDTOMapper;

    public ListingFacadeServiceImpl(WorkspaceService workspaceService,
                                    BoardService boardService,
                                    ListingService listingService,
                                    ListingDTOMapper listingDTOMapper){
        this.workspaceService = workspaceService;
        this.boardService = boardService;
        this.listingService = listingService;
        this.listingDTOMapper = listingDTOMapper;
    }

    @Override
    public List<ListingDTO> getListingsProcess(long workspaceId,
                                               long boardId){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        Optional<Board> board = boardService.getBoard(boardId);
        if(board.isEmpty()) throw new BoardException.BoardNotFound();
        return board
                .map(value -> listingService
                    .getListings(value)
                    .stream()
                    .map(listingDTOMapper)
                    .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<ListingDTO> getListingProcess(long workspaceId,
                                                  long boardId,
                                                  long listingId){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        if (boardService.getBoard(boardId).isEmpty())
            throw new BoardException.BoardNotFound();

        return listingService.getListing(listingId).map(listingDTOMapper);
    }

    @Override
    public Optional<ListingDTO> createListingProcess(long workspaceId,
                                                     long boardId,
                                                     ListingDTO listingDTO){
        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();

        Optional<Board> board = boardService.getBoard(boardId);
        if (board.isEmpty())
            throw new BoardException.BoardNotFound();

        else {
            return Optional.of(
                    listingService.createListing(listingDTO, board.get())).map(listingDTOMapper);
        }
    }

    @Override
    public Optional<ListingDTO> updateListingProcess(long workspaceId, long boardId,
                                                     long listingId, ListingDTO listingDTO){

        if (workspaceService.getWorkspace(workspaceId).isEmpty())
            throw new WorkspaceException.WorkspaceNotFound();


        Listing updatedListing = listingService.updateListing(listingId, listingDTO);
        return Optional.of(updatedListing).map(listingDTOMapper);
    }

    @Override
    public void deleteListingProcess(long workspaceId, long boardId, long listingId){
        Optional<Board> board = boardService.getBoard(boardId);
        if(board.isEmpty())
            throw new BoardException.BoardNotFound();
        listingService.deleteListing(listingId);
    }
}
