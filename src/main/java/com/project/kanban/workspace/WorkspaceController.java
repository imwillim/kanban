package com.project.kanban.workspace;

import com.project.kanban.util.RequestBodyError;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/")
public class WorkspaceController {
    private final WorkspaceFacadeService workspaceFacadeService;
    @Autowired

    public WorkspaceController(WorkspaceFacadeService workspaceFacadeService){
        this.workspaceFacadeService = workspaceFacadeService;
    }


    @GetMapping(path = "workspaces")
    public ResponseEntity<Object> getWorkspaces(){
        List<WorkspaceDTO> workspaces = workspaceFacadeService.getWorkspacesProcess();
        return ResponseEntity.ok().body(workspaces);
    }

    @GetMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> getWorkspace(@PathVariable("workspaceId") long workspaceId){
        Optional<WorkspaceDTO> workspace = workspaceFacadeService.getWorkspaceProcess(workspaceId);
        return ResponseEntity.ok().body(workspace);
    }


    @PostMapping(path = "workspaces")
    public ResponseEntity<Object> createWorkspace(@Valid @RequestBody WorkspaceDTO requestBody,
                                                  BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<WorkspaceDTO> createdWorkspace = workspaceFacadeService.createWorkspaceProcess(requestBody);
        URI createdWorkspaceURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/id}").buildAndExpand(createdWorkspace.get().getId()).toUri();
        return ResponseEntity.created(createdWorkspaceURI).build();
    }

    @PutMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> updateWorkspace(@PathVariable("workspaceId") long workspaceId,
                                                  @RequestBody WorkspaceDTO requestBody,
                                                  BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return RequestBodyError.returnRequiredFields(bindingResult);

        Optional<WorkspaceDTO> updatedWorkspace = workspaceFacadeService
                .updateWorkspaceProcess(workspaceId, requestBody);

        if (updatedWorkspace.isPresent())
            return ResponseEntity.accepted().body(updatedWorkspace);
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> deleteWorkspace(@PathVariable("workspaceId") long workspaceId){
        workspaceFacadeService.deleteWorkspaceProcess(workspaceId);
        return ResponseEntity.noContent().build();
    }

}
