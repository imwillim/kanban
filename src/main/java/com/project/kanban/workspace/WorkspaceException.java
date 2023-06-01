package com.project.kanban.workspace;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
public class WorkspaceException {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class WorkspaceNotFound extends EntityNotFoundException {
        public WorkspaceNotFound(){
            super("Workspace not found.");
            log.error("Workspace not found.");
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class WorkspaceDuplicated extends NonUniqueResultException {
        public WorkspaceDuplicated() {
            super("Workspace is duplicated.");
            log.error("Workspace is duplicated.");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class WorkspaceNotValidParams extends IllegalArgumentException {
        public WorkspaceNotValidParams() {
            super("Not valid parameters for Workspace entity.");
            log.error("Not valid parameters for Workspace entity.");
        }
    }
}
