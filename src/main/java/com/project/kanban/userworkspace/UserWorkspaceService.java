package com.project.kanban.userworkspace;

import java.util.List;
import java.util.Optional;

public interface UserWorkspaceService {
    List<UserWorkspace> getUserWorkspaces(long userId);

    Optional<UserWorkspace> getUserWorkspaceById(long id);

    Optional<UserWorkspace> getUserWorkspaceByUserAndWorkspaceIds(long userId, long workspaceId);

    void createUserWorkspace(long userId, long workspaceId, String role);

    void setRoleUserWorkspace(UserWorkspace workspaceUser, String role);

    String getRoleFromUserWorkspace(long userId, long workspaceId);

}
