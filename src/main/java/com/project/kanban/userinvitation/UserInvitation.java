package com.project.kanban.userinvitation;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity(name = "user_invitation")
@NoArgsConstructor
@Data
public class UserInvitation {
    @Id
    @SequenceGenerator(name = "userinvitation_sequence", sequenceName = "userinvitation_sequence", allocationSize = 1)
    @GeneratedValue(generator = "userinvitation_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "workspaceId")
    private long workspaceId;

    @Column(name = "ownerId")
    private long ownerId;

    @Column(name = "invitedId")
    private long invitedId;

    @Column(name = "created_at")
    private long createdAt;

    @Column(name = "updated_at")
    private long updatedAt;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private UserInvitationStatus status;

    public UserInvitation(long ownerId, long invitedId, long workspaceId, String status){
        this.ownerId = ownerId;
        this.invitedId = invitedId;
        this.workspaceId = workspaceId;
        this.createdAt = Timestamp.from(Instant.now()).getTime();
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.status = UserInvitationStatus.valueOf(status.toUpperCase());
    }
}
