package com.project.kanban.userworkspace;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "user_workspace")
@NoArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"user_id" , "workspace_id"})},
        indexes = @Index(name = "userworkspace_idx", columnList = "user_id, workspace_id")
)
public class UserWorkspace {
    @Id
    @SequenceGenerator(name = "userworkspace_sequence",
                        sequenceName = "userworkspace_sequence",
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userworkspace_sequence")
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "workspace_id")
    private long workspaceId;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserWorkspaceRole role;

    public UserWorkspace(long userId, long workspaceId, String role){
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.role = UserWorkspaceRole.valueOf(role.toUpperCase());
    }


}
