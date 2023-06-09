package com.project.kanban.workspace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceInviteRequest {
    private String invitedEmail;

    private String status;

}
