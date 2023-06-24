package com.project.kanban.board;

import com.project.kanban.util.RequestBodyError;
import com.project.kanban.util.RequestBodyException;
import com.project.kanban.util.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Object> getBoards(Authentication authentication, @PathVariable("workspaceId") long workspaceId){

        List<BoardDTO> boards = boardFacadeService.getBoardsProcess(authentication, workspaceId);
        return ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "List of boards returned",
                boards));
    }


    @GetMapping(path = "workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> getBoard(Authentication authentication,
            @PathVariable("workspaceId") long workspaceId,
            @PathVariable("boardId") long boardId){
        Optional<BoardDTO> board = boardFacadeService
                .getBoardProcess(authentication, workspaceId, boardId);

        if (board.isEmpty())
            return notFoundBoard();
        log.info("Board is returned.");
        return ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "Board is returned",
                board));
    }


    @PostMapping(path = "/workspaces/{workspaceId}/boards")
    public ResponseEntity<Object> createBoard(Authentication authentication,
                                              @PathVariable("workspaceId") long workspaceId,
                                              @Valid @RequestBody BoardDTO requestBody,
                                              BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<BoardDTO> createdBoard = boardFacadeService.createBoardProcess(authentication,
                workspaceId, requestBody);

        if(createdBoard.isPresent()) {

            URI createdBoardURI = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdBoard.get().getId()).toUri();
            log.info("New board is created.");
            return ResponseEntity.created(createdBoardURI).body(
                    new SuccessfulResponse(HttpStatus.CREATED.value(),
                            "New board is created",
                            createdBoard));
        }
        return notFoundBoard();
    }


    @PutMapping(path = "/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> updateBoard(Authentication authentication,
            @PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId,
                                              @Valid @RequestBody BoardDTO requestBody,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<BoardDTO> updatedBoard = boardFacadeService.updateBoardProcess(authentication, workspaceId,
                boardId, requestBody);

        log.info("Board is updated.");
        return ResponseEntity.accepted().body(new SuccessfulResponse(HttpStatus.ACCEPTED.value(),
                "Board is updated",
                updatedBoard));
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}/boards/{boardId}")
    public ResponseEntity<Object> deleteBoard(Authentication authentication,
                                              @PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId){
        if (boardFacadeService.getBoardProcess(authentication, workspaceId, boardId).isEmpty())
            return notFoundBoard();

        boardFacadeService.deleteBoardProcess(authentication, workspaceId, boardId);
        log.info("Board is deleted.");
        return new ResponseEntity<>(new SuccessfulResponse(
                HttpStatus.NO_CONTENT.value(),
                "Deleted a board",
                null), HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/workspaces/{workspaceId}/boards/{boardId}/join")
    public ResponseEntity<Object> assignBoard(Authentication authentication,
                                              @PathVariable("workspaceId") long workspaceId,
                                              @PathVariable("boardId") long boardId) {
        boardFacadeService.assignBoard(authentication, workspaceId, boardId);
        return  ResponseEntity.ok().body(new SuccessfulResponse(
                HttpStatus.OK.value(),
                "Join a board",
                null));

    }

}
