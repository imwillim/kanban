package com.project.kanban.workspace;

import com.project.kanban.auth.AuthException;
import com.project.kanban.auth.AuthService;
import com.project.kanban.user.User;
import com.project.kanban.user.UserException;
import com.project.kanban.user.UserService;
import com.project.kanban.userinvitation.UserInvitation;
import com.project.kanban.userinvitation.UserInvitationService;
import com.project.kanban.userworkspace.UserWorkspace;
import com.project.kanban.userworkspace.UserWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WorkspaceFacadeServiceImpl implements WorkspaceFacadeService {
    private final WorkspaceService workspaceService;
    private final WorkspaceDTOMapper workspaceDTOMapper;
    private final UserWorkspaceService userWorkspaceService;
    private final UserInvitationService userInvitationService;

    private final UserService userService;

    @Autowired
    public WorkspaceFacadeServiceImpl(WorkspaceService workspaceService,
                                      WorkspaceDTOMapper workspaceDTOMapper,
                                      UserWorkspaceService userWorkspaceService,
                                      UserInvitationService userInvitationService,
                                      UserService userService) {
        this.workspaceService = workspaceService;
        this.workspaceDTOMapper = workspaceDTOMapper;
        this.userWorkspaceService = userWorkspaceService;
        this.userInvitationService = userInvitationService;
        this.userService = userService;
    }

    @Override
    public List<WorkspaceDTO> getWorkspacesProcess(Authentication authentication) {
        return workspaceService
                .getAllWorkspaces()
                .stream()
                .map(workspaceDTOMapper)
                .toList();
    }

    @Override
    public Optional<WorkspaceDTO> getWorkspaceProcess(Authentication authentication, long workspaceId) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        userWorkspaceService.getUserWorkspaceByUserAndWorkspaceIds(userId, workspaceId)
                .orElseThrow(AuthException.ForbiddenError::new);

        return Optional.of(workspaceService.getWorkspace(workspaceId).map(workspaceDTOMapper))
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);
    }

    @Override
    public Optional<WorkspaceDTO> createWorkspaceProcess(Authentication authentication,
                                                         WorkspaceDTO workspaceDTO) {

        Optional<WorkspaceDTO> createdWorkspace = Optional
                .of(workspaceService.createWorkspace(workspaceDTO))
                .map(workspaceDTOMapper);

        if (createdWorkspace.isPresent()) {
            // Take id from authentication with JWT
            long userId = AuthService.getUserIdFromAuthentication(authentication);

            userWorkspaceService.createUserWorkspace(
                    userId,
                    createdWorkspace.get().getId(),
                    "OWNER");
        }

        return createdWorkspace;
    }

    @Override
    public Optional<WorkspaceDTO> updateWorkspaceProcess(Authentication authentication,
                                                         long workspaceId,
                                                         WorkspaceDTO workspaceDTO) {
        Optional<Workspace> workspace = workspaceService.getWorkspace(workspaceId);

        if (workspace.isEmpty()) {
            throw new WorkspaceException.WorkspaceNotFound();
        }
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspaceId);

        if (AuthService.checkAuthority(userWorkspaceRole, "OWNER")
                || AuthService.checkAuthority(userWorkspaceRole, "ADMIN")
                || AuthService.checkAuthority(userWorkspaceRole, "EDITOR"))
            return workspace.flatMap(value -> Optional.of(workspaceService.updateWorkspace(value, workspaceDTO))
                    .map(workspaceDTOMapper));
        throw new AuthException.ForbiddenError();
    }

    @Override
    public void deleteWorkspaceProcess(Authentication authentication, long workspaceId) {
        Optional.ofNullable(workspaceService.getWorkspace(workspaceId))
                .orElseThrow(WorkspaceException.WorkspaceNotFound::new);

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspaceId);

        if (AuthService.checkAuthority(userWorkspaceRole, "ADMIN"))
            workspaceService.deleteWorkspace(workspaceId);
        throw new AuthException.ForbiddenError();
    }

    @Override
    public void inviteMemberWorkspace(Authentication authentication, long workspaceId, WorkspaceInviteRequest otherUser) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspaceId);

        if (!AuthService.checkAuthority(userWorkspaceRole, "OWNER") &&
                !AuthService.checkAuthority(userWorkspaceRole, "ADMIN"))
            throw new AuthException.ForbiddenError();

        Optional<User> invitedUser = Optional.of(userService.getUserByEmail(otherUser.getInvitedEmail())
                .orElseThrow(UserException.UserNotFound::new));

        if (invitedUser.get().getId() == userId) // Check duplicate record
            throw new UserException.UserNotValidParams();

        Optional<UserInvitation> invitation = Optional.of(userInvitationService
                .getInvitationByOwnerAndInvited(invitedUser.get().getId(), workspaceId)).orElse(null);

        if (invitation.isPresent() && !invitation.get().getStatus().toString().equals("ACCEPTED"))
            userInvitationService.modifyUserInvitation(invitation.get(), "PENDING");

        userInvitationService.createUserInvitation(userId, invitedUser.get().getId(), workspaceId);
    }


    @Override
    public void modifyInvitation(Authentication authentication, long invitationId, WorkspaceInviteRequest request) {
        Optional<UserInvitation> invitation = userInvitationService.getInvitation(invitationId);

        Optional<UserInvitation> modifyInvitation = Optional.ofNullable(userInvitationService
                .modifyUserInvitation(invitation.get(), request.getStatus()));

        if (modifyInvitation.get().getStatus().toString().equals("ACCEPTED")) {
            long userId = AuthService.getUserIdFromAuthentication(authentication);

            userWorkspaceService.createUserWorkspace(userId, modifyInvitation.get().getWorkspaceId(), "EDITOR");
        }
    }

    @Override
    public void setRoleMemberWorkspace(Authentication authentication, long workspaceId,
                                       long roleUserId, String role) {

        long userId = AuthService.getUserIdFromAuthentication(authentication);
        String userWorkspaceRole = userWorkspaceService.getRoleFromUserWorkspace(userId, workspaceId);

        if (AuthService.checkAuthority(userWorkspaceRole, "OWNER")
                || AuthService.checkAuthority(userWorkspaceRole, "ADMIN")) {
            Optional<UserWorkspace> userWorkspace = userWorkspaceService
                    .getUserWorkspaceByUserAndWorkspaceIds(roleUserId, workspaceId);
            userWorkspaceService.setRoleUserWorkspace(userWorkspace.get(), role);
        }
        throw new AuthException.ForbiddenError();
    }

    @Override
    public List<UserInvitation> getInvitations(Authentication authentication) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        return userInvitationService.getListInvited(userId)
                .stream()
                .filter(inv -> inv.getStatus().toString().equals("PENDING"))
                .toList();
    }


    @Override
    public List<UserInvitation> getListOfInvitedUsers(Authentication authentication) {
        long userId = AuthService.getUserIdFromAuthentication(authentication);
        return userInvitationService.getListInviteOwner(userId)
                .stream()
                .filter(inv -> inv.getStatus().toString().equals("PENDING"))
                .toList();
    }
}





