package com.project.kanban.userboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findUserBoardByUserIdAndBoardId(long userId, long boardId);

}
