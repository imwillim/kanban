package com.project.kanban.userworkspace;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {
    Optional<UserWorkspace> findUserWorkspaceByUserIdAndWorkspaceId(long userId, long workspaceId);
    List<UserWorkspace> findUserWorkspacesByUserId(long userId);
}
