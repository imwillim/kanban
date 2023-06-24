package com.project.kanban.user;


import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long userId);
    User createUser(UserRequest requestBody);
    User updateUser(User updatedUser, UserRequest requestBody);
    void deleteUser(long userId);
    String getEmailFromJwt(Authentication authentication);
    Optional<User> getUserByEmail(String email);
}
