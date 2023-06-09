package com.project.kanban.usercard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCardServiceImpl implements UserCardService{
    private final UserCardRepository userCardRepository;
    @Autowired
    public UserCardServiceImpl(UserCardRepository userCardRepository){
        this.userCardRepository = userCardRepository;
    }

    @Override
    public Optional<UserCard> getUserCardById(long id) {
        return userCardRepository.findById(id);
    }

    @Override
    public Optional<UserCard> getUserCardByUserAndCardIds(long userId, long cardId) {
        return userCardRepository.findUserCardByUserIdAndCardId(userId, cardId);
    }

    @Override
    public void createUserCard(long userId, long cardId, String role) {
        UserCard userCard = new UserCard(userId, cardId, role);
        userCardRepository.save(userCard);
    }

    @Override
    public String getRoleFromUserCard(long userId, long cardId){
        Optional<UserCard> userCard = getUserCardByUserAndCardIds(userId, cardId);
        if (userCard.isPresent()){
            return userCard.get().getRole().toString();
        }
        return "";
    }

    @Override
    public void setRoleUserCard(UserCard userCard, String role) {
        userCard.setRole(UserCardRole.valueOf(role));
        userCardRepository.save(userCard);
    }
}
