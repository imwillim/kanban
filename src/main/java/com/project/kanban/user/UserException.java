package com.project.kanban.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class UserException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UserNotFound extends EntityNotFoundException {
        public UserNotFound() {
            super("User not found.");
            log.error("User not found.");
        }
    }
    @ResponseStatus(HttpStatus.CONFLICT)
    public static class UserEmailDuplicated extends NonUniqueResultException {
        public UserEmailDuplicated() {
            super("Email is duplicated.");
            log.error("Email is duplicated.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UserNotValidParams extends IllegalArgumentException {
        public UserNotValidParams() {
            super("Not valid parameters for User entity.");
            log.error("Not valid parameters for User entity.");
        }
    }
}
