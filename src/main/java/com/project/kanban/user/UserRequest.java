package com.project.kanban.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    @NotNull(message = "Username must not be null")
    private String username;

    @Pattern(regexp = "^(.+)@(.+)$", message = "Email must be valid")
    @NotNull(message = "Email must not be null")
    private String email;
    // Minimum eight characters, at least one uppercase letter, one lowercase letter,
    // one number and one special character:
   @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message =  "Password must have minimum eight characters, at least one uppercase letter," +
                    " one lowercase letter, one number and one special character")
    private String password;

}
