package com.project.kanban.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    private String username;
    private String email;
    private long updatedAt;
    private long createdAt;
    public UserDTO(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
