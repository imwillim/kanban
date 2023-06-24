package com.project.kanban.usercard;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_card")
@NoArgsConstructor
@Data
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"user_id" , "card_id"})})

public class UserCard {
    @Id
    @SequenceGenerator(name = "usercard_sequence",
            sequenceName = "usercard_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usercard_sequence")
    @Column(name = "id")
    private long id;

    @Column(name = "user_id")
    private long userId;
    @Column(name = "card_id")
    private long cardId;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserCardRole role;


    public UserCard(long userId, long cardId, String role){
        this.userId = userId;
        this.cardId = cardId;
        this.role = UserCardRole.valueOf(role.toUpperCase());
    }


}
