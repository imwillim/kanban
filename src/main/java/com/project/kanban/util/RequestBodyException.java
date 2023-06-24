package com.project.kanban.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j

public class RequestBodyException {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestBody extends IllegalArgumentException {
        public BadRequestBody(StringBuilder message){
            super(message.toString());
            log.error(message.toString());
        }
    }
}
