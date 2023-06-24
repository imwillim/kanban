package com.project.kanban.card;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class CardException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CardNotFound extends EntityNotFoundException {
        public CardNotFound(){
            super("Card not found.");
            log.error("Card not found.");
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class CardDuplicated extends NonUniqueResultException {
        public CardDuplicated() {
            super("Card is duplicated.");
            log.error("Card is duplicated.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CardNotValidParams extends IllegalArgumentException {
        public CardNotValidParams() {
            super("Not valid parameters for Card entity.");
            log.error("Not valid parameters for Card entity.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class CardNotValidId extends NumberFormatException {
        public CardNotValidId() {
            super("Id for Card must be a number.");
            log.error("Id for Card must be a number.");
        }
    }
}
