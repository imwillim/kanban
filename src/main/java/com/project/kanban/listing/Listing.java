package com.project.kanban.listing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.kanban.board.Board;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Listing")
@Data
@NoArgsConstructor
public class Listing {
    @Id
    @SequenceGenerator(name = "listing_sequence", sequenceName = "listing_sequence", allocationSize = 1)
    @GeneratedValue(generator = "listing_sequence", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "type")
    private String type;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "board_id")
    private Board board;

    public Listing(String title, String type, Board board){
        this.title = title;
        this.type = type;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.board = board;
    }
}
