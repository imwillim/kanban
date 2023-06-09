package com.project.kanban.userinvitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInvitationServiceImpl implements UserInvitationService{

    private final UserInvitationRepository userInvitationRepository;

    @Autowired
    public UserInvitationServiceImpl(UserInvitationRepository userInvitationRepository){
        this.userInvitationRepository = userInvitationRepository;
    }

    @Override
    public Optional<UserInvitation> getInvitationByOwnerAndInvited(long ownerId, long invitedId){
        return userInvitationRepository.getUserInvitationByOwnerIdAndInvitedId(ownerId, invitedId);
    }

    @Override
    public Optional<UserInvitation> getInvitation(long id) {
        return userInvitationRepository.findById(id);
    }

    @Override
    public List<UserInvitation> getListInvited(long invitedId) {
        return userInvitationRepository.getUserInvitationsByInvitedId(invitedId);
    }

    @Override
    public List<UserInvitation> getListInviteOwner(long ownerId) {
        return userInvitationRepository.getUserInvitationsByOwnerId(ownerId);
    }

    @Override
    public void createUserInvitation(long ownerId, long invitedId, long workspaceId) {
        UserInvitation userInvitation = new UserInvitation(ownerId, invitedId, workspaceId, "PENDING");
        userInvitationRepository.save(userInvitation);
    }


    @Override
    public UserInvitation modifyUserInvitation(UserInvitation userInvitation, String status){
        userInvitation.setStatus(UserInvitationStatus.valueOf(status.toUpperCase()));
        return userInvitationRepository.save(userInvitation);
    }
}
