package com.project.kanban.user;

import java.util.Optional;

public interface UserFacadeService {
    Optional<UserDTO> getUserProcess(long userId);
    Optional<UserDTO> createUserProcess(UserRequest requestBody);
    Optional<UserDTO> updateUserProcess(long userId, UserRequest requestBody);
    void deleteUserProcess(long userId);
}
