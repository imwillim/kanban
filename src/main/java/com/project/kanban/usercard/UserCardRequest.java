package com.project.kanban.usercard;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardRequest {
    private String username;
    private String role;

}
