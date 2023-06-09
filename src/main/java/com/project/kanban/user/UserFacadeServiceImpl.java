package com.project.kanban.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFacadeServiceImpl implements UserFacadeService {
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public UserFacadeServiceImpl(UserService userService, UserDTOMapper userDTOMapper){
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }
    @Override
    public Optional<UserDTO> getUserProcess(long userId) {
        return Optional.ofNullable(userService
                .getUserById(userId)
                .map(userDTOMapper)
                .orElseThrow(UserException.UserNotFound::new));
    }

    @Override
    public Optional<UserDTO> createUserProcess(UserRequest requestBody) {

        Optional<User> existedUser = userService.getUserByEmail(requestBody.getEmail());

        if (existedUser.isPresent())
            throw new UserException.UserEmailDuplicated();

        return Optional.of(userService
                .createUser(requestBody))
                .map(userDTOMapper);
    }

    @Override
    public Optional<UserDTO> updateUserProcess(long userId, UserRequest requestBody) {
        Optional<User> user = Optional.ofNullable(userService
                .getUserById(userId).orElseThrow(UserException.UserNotFound::new));

        return Optional.of(userService.updateUser(user.get(), requestBody)).map(userDTOMapper);
    }

    @Override
    public void deleteUserProcess(long userId) {
        userService.deleteUser(userId);
    }

}
