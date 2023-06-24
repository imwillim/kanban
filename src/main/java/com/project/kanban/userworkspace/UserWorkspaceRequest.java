package com.project.kanban.userworkspace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWorkspaceRequest {
    private String email;
    private String role;
    private String status;

    public UserWorkspaceRequest(String email, String role){
        this.email = email;
        this.role = role;
    }

    public UserWorkspaceRequest(String status){
        this.status = status;
    }
}
