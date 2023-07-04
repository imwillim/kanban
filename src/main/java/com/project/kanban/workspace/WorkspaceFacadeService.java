package com.project.kanban.workspace;

import com.project.kanban.userinvitation.UserInvitation;
import com.project.kanban.userworkspace.UserWorkspaceRequest;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface WorkspaceFacadeService {
    List<WorkspaceDTO> getWorkspacesProcess(Authentication authentication);
    Optional<WorkspaceDTO> getWorkspaceProcess(Authentication authentication, long workspaceId);
    Optional<WorkspaceDTO> createWorkspaceProcess(Authentication authentication, WorkspaceDTO workspaceDTO);
    Optional<WorkspaceDTO> updateWorkspaceProcess(Authentication authentication, long workspaceId, WorkspaceDTO workspaceDTO);
    void deleteWorkspaceProcess(Authentication authentication, long workspaceId);
    void inviteMemberWorkspace(Authentication authentication, long workspaceId, WorkspaceInviteRequest email);
    List<UserInvitation> getInvitations(Authentication authentication);
    void modifyInvitation(Authentication authentication, long userWorkspaceId, WorkspaceInviteRequest status);
    void setRoleMemberWorkspace(Authentication authentication, long workspaceId, long roleUserId,
                                UserWorkspaceRequest userWorkspaceRequest);
    List<UserInvitation> getListOfInvitedUsers(Authentication authentication);
}
