package com.project.kanban.userinvitation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInvitationRepository extends JpaRepository<UserInvitation, Long> {
    Optional<UserInvitation> getUserInvitationByOwnerIdAndInvitedId(long ownerId, long invitedId);

    List<UserInvitation> getUserInvitationsByOwnerId(long ownerId);

    List<UserInvitation> getUserInvitationsByInvitedId(long invitedId);
}
