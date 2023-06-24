package com.project.kanban.userboard;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserBoardServiceImpl implements UserBoardService {
    private final UserBoardRepository userBoardRepository;
    public UserBoardServiceImpl(UserBoardRepository userBoardRepository){
        this.userBoardRepository = userBoardRepository;
    }
    @Override
    public Optional<UserBoard> getUserBoardById(long id) {
        return userBoardRepository.findById(id);
    }

    @Override
    public Optional<UserBoard> getUserBoardByUserAndBoardIds(long userId, long boardId) {
        return userBoardRepository.findUserBoardByUserIdAndBoardId(userId, boardId);
    }

    @Override
    public void createUserBoard(long userId, long boardId, String role) {
        UserBoard boardAssignee = new UserBoard(userId, boardId, role);
        userBoardRepository.save(boardAssignee);
    }

    @Override
    public String getRoleFromUserBoard(long userId, long boardId){
        Optional<UserBoard> userBoard = getUserBoardByUserAndBoardIds(userId, boardId);
        if (userBoard.isPresent()){
            return userBoard.get().getRole().toString();
        }
        return "";
    }
    @Override
    public void setRoleUserBoard(UserBoard userBoard, String role) {
        userBoard.setRole(UserBoardRole.valueOf(role));
        userBoardRepository.save(userBoard);
    }
}
