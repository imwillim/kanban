package com.project.kanban.workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceService {
    Optional<Workspace> getWorkspace(long workspaceId);
    List<Workspace> getAllWorkspaces();

    Workspace createWorkspace(WorkspaceDTO workspaceDTO);

    Workspace updateWorkspace(Workspace workspace, WorkspaceDTO workspaceDTO);

    void deleteWorkspace(long workspaceId);
    
}
