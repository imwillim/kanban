package com.project.kanban.workspace;

import com.project.kanban.board.BoardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository){
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    @Override
    public Optional<Workspace> getWorkspace(long workspaceId){
        return workspaceRepository.findById(workspaceId);
    }


    @Override
    public Workspace createWorkspace(WorkspaceDTO workspaceDTO) {
        Workspace createdWorkspace = new Workspace(workspaceDTO.getTitle(), workspaceDTO.getDescription());
        return workspaceRepository.save(createdWorkspace);
    }


    @Override
    public Workspace updateWorkspace(Workspace workspace, WorkspaceDTO workspaceDTO){
        if (workspace != null) {
            workspace.setTitle(workspaceDTO.getTitle());
            workspace.setDescription(workspaceDTO.getDescription());
            workspace.setUpdatedAt(LocalDateTime.now());
            return workspaceRepository.save(workspace);
        }
        return null;
    }



    @Override
    public void deleteWorkspace(long workspaceId){
        if (getWorkspace(workspaceId).isEmpty())
            throw new BoardException.BoardNotFound();
        else
            workspaceRepository.deleteById(workspaceId);
    }
}
