package com.project.kanban.workspace;

import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service

public class WorkspaceDTOMapper implements Function<Workspace, WorkspaceDTO> {
    @Override
    public WorkspaceDTO apply(Workspace workspace) {
        return new WorkspaceDTO(
                workspace.getId(),
                workspace.getTitle(),
                workspace.getDescription()
        );
    }
}
