package com.project.kanban.user;

import com.project.kanban.util.PasswordUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User createUser(UserRequest requestBody) {
        String encodedPassword = PasswordUtil.encode(requestBody.getPassword());
        User createdUser = new User(requestBody.getUsername(),
                                    requestBody.getEmail(),
                                    encodedPassword);
        return userRepository.save(createdUser);
    }

    @Override
    public User updateUser(User updatedUser, UserRequest requestBody) {
        updatedUser.setUsername(requestBody.getUsername());
        String encodedPassword = PasswordUtil.encode(requestBody.getPassword());
        updatedUser.setPassword(encodedPassword);
        updatedUser.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()).getTime());
        return userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public UserCustomDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(UserException.UserEmailDuplicated::new);

        return user.map(UserCustomDetail::new).orElse(null);

    }

    @Override
    public String getEmailFromJwt(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
