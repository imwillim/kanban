package com.project.kanban.util;

import com.project.kanban.board.BoardException;
import com.project.kanban.card.CardException;
import com.project.kanban.listing.ListingException;
import com.project.kanban.user.UserException;
import com.project.kanban.workspace.WorkspaceException;
import com.project.kanban.auth.AuthException;
import com.project.kanban.jwt.JwtAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class KanbanExceptionHandler {
    @ExceptionHandler ({
            WorkspaceException.WorkspaceNotFound.class,
            BoardException.BoardNotFound.class,
            ListingException.ListingNotFound.class,
            CardException.CardNotFound.class,
            UserException.UserNotFound.class})
    public ResponseEntity<ResponseError> handleNotFound(Exception notFoundException){
        return new ResponseEntity<>(
                new ResponseError(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        notFoundException.getMessage()
                ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler ({
            JwtAuthException.Unauthorized.class,
            WorkspaceException.WorkspaceNotValidId.class,
            BoardException.BoardNotValidId.class,
            ListingException.ListingNotValidId.class,
            CardException.CardNotValidId.class,
            RequestBodyException.BadRequestBody.class,
            AuthException.LoginFailed.class,
            JwtAuthException.ExpiredToken.class,
            JwtAuthException.Unauthorized.class,
            ServletException.ServletResponse.class
    })
    public ResponseEntity<ResponseError> handleNotValidParams(Exception notValidParamsException){
        return new ResponseEntity<>(
                new ResponseError(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        notValidParamsException.getMessage()
                ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ({
            WorkspaceException.WorkspaceDuplicated.class,
            BoardException.BoardDuplicated.class,
            ListingException.ListingDuplicated.class,
            CardException.CardDuplicated.class,
            UserException.UserEmailDuplicated.class})
    public ResponseEntity<ResponseError> handleDuplicatedEntity(Exception existedEntityException){
        return new ResponseEntity<>(
                new ResponseError(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT.value(),
                        existedEntityException.getMessage()
                ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthException.ForbiddenError.class)
    public ResponseEntity<ResponseError> notExactRole(Exception notAcessException){
        return new ResponseEntity<>(
                new ResponseError(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "Forbidden."
                ), HttpStatus.UNAUTHORIZED);
    }
}
