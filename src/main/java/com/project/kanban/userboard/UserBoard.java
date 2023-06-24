package com.project.kanban.userboard;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "user_board")
@NoArgsConstructor
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"user_id" , "board_id"})})
public class UserBoard {
    @Id
    @SequenceGenerator(name = "userboard_sequence",
            sequenceName = "userboard_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userboard_sequence")
    @Column(name = "id")
    private long id;
    @Column(name = "board_id")
    private long boardId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private UserBoardRole role;

    public UserBoard(long userId, long boardId, String role){
        this.userId = userId;
        this.boardId = boardId;
        this.role = UserBoardRole.valueOf(role);
    }

}
