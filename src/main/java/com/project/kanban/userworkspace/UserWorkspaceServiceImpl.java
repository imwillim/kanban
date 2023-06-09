package com.project.kanban.userworkspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserWorkspaceServiceImpl implements UserWorkspaceService {
    private final UserWorkspaceRepository userWorkspaceRepository;
    @Autowired
    public UserWorkspaceServiceImpl(UserWorkspaceRepository userWorkspaceRepository) {
        this.userWorkspaceRepository = userWorkspaceRepository;
    }

    @Override
    public List<UserWorkspace> getUserWorkspaces(long userId){
        return userWorkspaceRepository.findUserWorkspacesByUserId(userId);
    }
    @Override
    public Optional<UserWorkspace> getUserWorkspaceById(long id) {
        return userWorkspaceRepository.findById(id);
    }

    @Override
    public Optional<UserWorkspace> getUserWorkspaceByUserAndWorkspaceIds(long userId, long workspaceId) {
        return userWorkspaceRepository.findUserWorkspaceByUserIdAndWorkspaceId(userId, workspaceId);
    }

    @Override
    public void createUserWorkspace(long userId, long workspaceId, String role) {
        UserWorkspace userWorkspace = new UserWorkspace(userId, workspaceId, role);
        userWorkspaceRepository.save(userWorkspace);
    }

    @Override
    public void setRoleUserWorkspace(UserWorkspace userWorkspace, String role) {
        userWorkspace.setRole(UserWorkspaceRole.valueOf(role.toUpperCase()));
        userWorkspaceRepository.save(userWorkspace);
    }

    @Override
    public String getRoleFromUserWorkspace(long userId, long workspaceId){
        Optional<UserWorkspace> userWorkspace = getUserWorkspaceByUserAndWorkspaceIds(userId, workspaceId);
        if (userWorkspace.isPresent()){
            return userWorkspace.get().getRole().toString();
        }
        return "";
    }



}
