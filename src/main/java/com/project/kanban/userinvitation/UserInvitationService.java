package com.project.kanban.userinvitation;

import java.util.List;
import java.util.Optional;

public interface UserInvitationService {
    Optional<UserInvitation> getInvitationByOwnerAndInvited(long ownerId, long invitedId);

    Optional<UserInvitation> getInvitation(long id);
    List<UserInvitation> getListInvited(long invitedId);
    List<UserInvitation> getListInviteOwner(long invitedId);
    void createUserInvitation(long ownerId, long invitedId, long workspaceId);
    UserInvitation modifyUserInvitation(UserInvitation userInvitation, String status);

}
