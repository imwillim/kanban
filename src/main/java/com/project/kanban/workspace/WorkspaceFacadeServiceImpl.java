package com.project.kanban.workspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceFacadeServiceImpl implements WorkspaceFacadeService {
    private final WorkspaceService workspaceService;
    private final WorkspaceDTOMapper workspaceDTOMapper;
    @Autowired
    public WorkspaceFacadeServiceImpl(WorkspaceService workspaceService, WorkspaceDTOMapper workspaceDTOMapper){
        this.workspaceService = workspaceService;
        this.workspaceDTOMapper = workspaceDTOMapper;
    }

    @Override
    public List<WorkspaceDTO> getWorkspacesProcess() {
        return workspaceService
                .getAllWorkspaces()
                .stream()
                .map(workspaceDTOMapper)
                .toList();
    }

    @Override
    public Optional<WorkspaceDTO> getWorkspaceProcess(long workspaceId) {
        return Optional.of(workspaceService.getWorkspace(workspaceId).map(workspaceDTOMapper))
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);
    }

    @Override
    public Optional<WorkspaceDTO> createWorkspaceProcess(WorkspaceDTO workspaceDTO) {
        return Optional.of(workspaceService.createWorkspace(workspaceDTO))
                .map(workspaceDTOMapper);
    }

    @Override
    public Optional<WorkspaceDTO> updateWorkspaceProcess(long workspaceId, WorkspaceDTO workspaceDTO) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);

        if (workspace.isEmpty()){
            throw new WorkspaceException.WorkspaceNotFound();
        }

        return workspace.flatMap(value -> Optional.of(workspaceService.updateWorkspace(value, workspaceDTO))
                .map(workspaceDTOMapper));
    }

    @Override
    public void deleteWorkspaceProcess(long workspaceId) {
        Optional.ofNullable(workspaceService.getWorkspace(workspaceId))
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        workspaceService.deleteWorkspace(workspaceId);
    }
}
