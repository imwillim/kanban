package com.project.kanban.board;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class BoardException  {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class BoardNotFound extends EntityNotFoundException {
        public BoardNotFound() {
            super("Board not found");
            log.error("Board not found");
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class BoardDuplicated extends NonUniqueResultException {
        public BoardDuplicated() {
            super("Board is duplicated");
            log.error("Board is duplicated");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BoardNotValidParams extends IllegalArgumentException {
        public BoardNotValidParams() {
            super("Not valid parameters for Board entity.");
            log.error("Not valid parameters for Board entity.");
        }
    }



}
