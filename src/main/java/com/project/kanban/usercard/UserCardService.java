package com.project.kanban.usercard;


import java.util.Optional;

public interface UserCardService {
    Optional<UserCard> getUserCardById(long id);
    Optional<UserCard> getUserCardByUserAndCardIds(long userId, long cardId);
    void createUserCard(long userId, long cardId, String role);

    String getRoleFromUserCard(long userId, long cardId);

    void setRoleUserCard(UserCard userCard, String role);

}
