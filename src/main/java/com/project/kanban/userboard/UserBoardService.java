package com.project.kanban.userboard;

import java.util.Optional;

public interface UserBoardService {
    Optional<UserBoard> getUserBoardById(long id);
    Optional<UserBoard> getUserBoardByUserAndBoardIds(long userId, long boardId);

    void createUserBoard(long userId, long boardId, String role);

    String getRoleFromUserBoard(long userId, long boardId);

    void setRoleUserBoard(UserBoard boardAssignee, String role);
}
