package com.project.kanban.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class AuthException {
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class ForbiddenError extends IllegalAccessError {
        public ForbiddenError() {
            super("User is forbidden.");
            log.error("User is forbidden.");
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class LoginFailed extends IllegalAccessError {
            public LoginFailed(){
                super("Invalid email or password");
                log.error("Invalid email or password");
            }
        }
}


