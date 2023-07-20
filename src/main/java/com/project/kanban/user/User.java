package com.project.kanban.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.sql.Timestamp;
import java.time.Instant;


@Entity(name = "kanban_user")
@Data
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "user_email_idx", columnList = "email")
})
@ToString
public class User {
    @Id
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(generator = "user_sequence", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    // Minimum eight characters, at least one uppercase letter, one lowercase letter,
    // one number and one special character:
    private String password;
    @Column(name = "updated_at")
    private long updatedAt;
    @Column(name = "created_at")
    private long createdAt;


    public User(String username, String email, String encodedPassword){
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }

}
