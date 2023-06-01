package com.project.kanban.util;

import com.project.kanban.board.BoardException;
import com.project.kanban.card.CardException;
import com.project.kanban.listing.ListingException;
import com.project.kanban.workspace.WorkspaceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class KanbanExceptionHandler {
    @ExceptionHandler ({WorkspaceException.WorkspaceNotFound.class,
                        BoardException.BoardNotFound.class,
                        ListingException.ListingNotFound.class,
                        CardException.CardNotFound.class})
    public ResponseEntity<?> handleNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler ({WorkspaceException.WorkspaceNotValidParams.class,
                        BoardException.BoardNotValidParams.class,
                        ListingException.ListingNotValidParams.class,
                        CardException.CardNotValidParams.class})
    public ResponseEntity<Exception> handleNotValidParams(Exception exception){
        return ResponseEntity.badRequest().body(exception);
    }

    @ExceptionHandler ({WorkspaceException.WorkspaceDuplicated.class,
                        BoardException.BoardDuplicated.class,
                        ListingException.ListingDuplicated.class,
                        CardException.CardDuplicated.class})
    public ResponseEntity<Exception> handleDuplicatedEntity(Exception exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }
}
