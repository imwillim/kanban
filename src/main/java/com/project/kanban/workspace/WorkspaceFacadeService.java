package com.project.kanban.workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceFacadeService {
    List<WorkspaceDTO> getWorkspacesProcess();
    Optional<WorkspaceDTO> getWorkspaceProcess(long workspaceId);
    Optional<WorkspaceDTO> createWorkspaceProcess(WorkspaceDTO workspaceDTO);
    Optional<WorkspaceDTO> updateWorkspaceProcess(long workspaceId, WorkspaceDTO workspaceDTO);
    void deleteWorkspaceProcess(long workspaceId);
}
