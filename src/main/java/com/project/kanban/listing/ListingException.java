package com.project.kanban.listing;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class ListingException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class ListingNotFound extends EntityNotFoundException {
        public ListingNotFound(){
            super("Listing not found.");
            log.error("Listing not found.");
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class ListingDuplicated extends NonUniqueResultException {
        public ListingDuplicated() {
            super("Listing is duplicated.");
            log.error("Listing is duplicated.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ListingNotValidParams extends IllegalArgumentException {
        public ListingNotValidParams() {
            super("Not valid parameters for Listing entity.");
            log.error("Not valid parameters for Listing entity.");
        }
    }
}
