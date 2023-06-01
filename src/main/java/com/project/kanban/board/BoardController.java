package com.project.kanban.board;

import com.project.kanban.util.RequestBodyError;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1")
@Slf4j
public class BoardController {
    private final BoardFacadeService boardFacadeService;
    @Autowired
    public BoardController(BoardFacadeService boardFacadeService){
        this.boardFacadeService = boardFacadeService;
    }

    private static ResponseEntity<Object> notFoundBoard(){
        log.error("Board not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/workspaces/{workspaceId}/boards")
    public ResponseEntity<List<BoardDTO>> getBoards(@PathVariable("workspaceId") long workspaceId){
        return ResponseEntity.ok().body(boardFacadeService.getBoardsProcess(workspaceId));
    }


    @GetMapping(path = "workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> getBoard(@PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId){
        Optional<BoardDTO> board = boardFacadeService.getBoardProcess(workspaceId, boardId);
        if (board.isEmpty())
            return notFoundBoard();
        return ResponseEntity.ok().body(board);
    }


    @PostMapping(path = "/workspaces/{workspaceId}/boards")
    public ResponseEntity<Object> createBoard(@PathVariable("workspaceId") long workspaceId,
                                            @Valid @RequestBody BoardDTO requestBody,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<BoardDTO> createdBoard = boardFacadeService.createBoardProcess(workspaceId, requestBody);

        if(createdBoard.isPresent()) {
            URI createdBoardURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdBoard.get().getId()).toUri();
            return ResponseEntity.created(createdBoardURI).build();
        }
        return notFoundBoard();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> updateBoard(@PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId,
                                              @Valid @RequestBody BoardDTO requestBody,
                                              BindingResult bindingResult) {
        if (boardFacadeService.getBoardProcess(workspaceId, boardId).isEmpty())
            return notFoundBoard();

        if(bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<BoardDTO> updatedBoard = boardFacadeService.updateBoardProcess(workspaceId, boardId, requestBody);
        return ResponseEntity.accepted().body(updatedBoard);
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> deleteBoard(@PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId){
        if (boardFacadeService.getBoardProcess(workspaceId, boardId).isEmpty())
            return notFoundBoard();

        boardFacadeService.deleteBoardProcess(workspaceId, boardId);
        return ResponseEntity.noContent().build();
    }
}
