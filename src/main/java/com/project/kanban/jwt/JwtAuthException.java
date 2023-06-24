package com.project.kanban.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j

public class JwtAuthException {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class Unauthorized extends JwtException {
        public Unauthorized(String message){
            super(message);
            log.error(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class ExpiredToken extends ExpiredJwtException {
        public ExpiredToken (String message){
            super(null, null, message);
            log.error("Expired JWT token");
        }
    }


}
