package com.project.kanban.workspace;

import com.project.kanban.userinvitation.UserInvitation;
import com.project.kanban.userworkspace.UserWorkspace;
import com.project.kanban.util.RequestBodyError;
import com.project.kanban.util.RequestBodyException;
import com.project.kanban.util.SuccessfulResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<Object> getWorkspaces(Authentication authentication){
        List<WorkspaceDTO> workspaces = workspaceFacadeService
                .getWorkspacesProcess(authentication);
        log.info("List of workspaces is returned.");
        return ResponseEntity.ok().body(
                new SuccessfulResponse(HttpStatus.OK.value(),
                        "List of workspaces is returned",
                        workspaces));
    }

    @GetMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> getWorkspace(Authentication authentication,
                                                @PathVariable("workspaceId") long workspaceId){
        Optional<WorkspaceDTO> workspace = workspaceFacadeService
                .getWorkspaceProcess(authentication, workspaceId);
        log.info("Workspace is returned.");
        return ResponseEntity.ok().body(new SuccessfulResponse(HttpStatus.OK.value(),
                "Workspace is returned",
                workspace));
    }


    @PostMapping(path = "workspaces")
    public ResponseEntity<Object> createWorkspace(Authentication authentication,
                                                    @Valid @RequestBody WorkspaceDTO requestBody,
                                                  BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<WorkspaceDTO> createdWorkspace =
                workspaceFacadeService.createWorkspaceProcess(authentication, requestBody);

        URI createdWorkspaceURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/id}").buildAndExpand(createdWorkspace.get().getId()).toUri();

        log.info("Created a new workspace.");
        return ResponseEntity.created(createdWorkspaceURI).body(
                new SuccessfulResponse(HttpStatus.CREATED.value(),
                        "Workspace is created",
                        createdWorkspace));
    }

    @PutMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> updateWorkspace(Authentication authentication,
                                                  @PathVariable("workspaceId") long workspaceId,
                                                  @RequestBody WorkspaceDTO requestBody,
                                                  BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        Optional<WorkspaceDTO> updatedWorkspace = workspaceFacadeService
                .updateWorkspaceProcess(authentication, workspaceId, requestBody);

        log.info("Workspace is updated");
        if (updatedWorkspace.isPresent())
            return ResponseEntity.accepted().body(
                    new SuccessfulResponse(HttpStatus.ACCEPTED.value(),
                            "Workspace is updated",
                            updatedWorkspace));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping(path = "workspaces/{workspaceId}")
    public ResponseEntity<Object> deleteWorkspace(Authentication authentication,
                                                  @PathVariable("workspaceId") long workspaceId){
        workspaceFacadeService.deleteWorkspaceProcess(authentication, workspaceId);
        log.info("Workspace is deleted");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.NO_CONTENT.value(),
                        "Workspace is deleted",
                        null), HttpStatus.NO_CONTENT);
    }


    @PostMapping(path = "workspaces/{workspaceId}/invite")
    public ResponseEntity<Object> inviteMemberWorkspace(Authentication authentication,
                                                  @PathVariable("workspaceId") long workspaceId,
                                                        @RequestBody WorkspaceInviteRequest invitedUser,
                                                        BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }

        workspaceFacadeService.inviteMemberWorkspace(authentication, workspaceId, invitedUser);
        log.info("Invited a user to workspace.");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.CREATED.value(),
                        "Invited a user to workspace.",
                        null), HttpStatus.CREATED);
    }

    @PatchMapping(path = "invitations/{invitationId}")
    public ResponseEntity<Object> modifyInvitation(Authentication authentication,
                                                   @PathVariable("invitationId") long invitationId,
                                                   @RequestBody WorkspaceInviteRequest status,
                                                   BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder message = RequestBodyError.returnRequiredFields(bindingResult);
            throw new RequestBodyException.BadRequestBody(message);
        }
        workspaceFacadeService.modifyInvitation(authentication, invitationId, status);
        log.info("Modified an invitation.");
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.OK.value(),
                        "Modified an invitation",
                        null), HttpStatus.OK);
    }

    @GetMapping(path = "invited")
    public ResponseEntity<Object> getListsInvitedUser(Authentication authentication){
        List<UserInvitation> invitations = workspaceFacadeService.getListOfInvitedUsers(authentication);
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.OK.value(),
                        "Get a list of invitations",
                        invitations), HttpStatus.OK);
    }


    @GetMapping(path = "invitations")
    public ResponseEntity<Object> getInvitations(Authentication authentication){
        List<UserInvitation> invitations = workspaceFacadeService.getInvitations(authentication);
        return new ResponseEntity<>(
                new SuccessfulResponse(HttpStatus.OK.value(),
                        "Get a list of invitations",
                        invitations), HttpStatus.OK);
    }

}
