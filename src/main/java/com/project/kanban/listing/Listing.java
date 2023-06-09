package com.project.kanban.listing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.kanban.board.Board;
import com.project.kanban.card.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "listing")
@Data
@NoArgsConstructor
public class Listing {
    @Id
    @SequenceGenerator(name = "listing_sequence", sequenceName = "listing_sequence", allocationSize = 1)
    @GeneratedValue(generator = "listing_sequence", strategy = GenerationType.SEQUENCE)
    private long id;


    @Column(name = "title")
    @Size(min = 3, max = 50, message = "Title of card have a length from 3 to 50 characters.")
    @NotNull(message = "Title of listing must not be null.")
    private String title;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ListingType type;

    @Column(name = "archived")
    private boolean isArchived;

    @Column(name = "updated_at")
    private long updatedAt;

    @Column(name = "created_at")
    private long createdAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listing")
    @JsonManagedReference
    private List<Card> cards;

    public Listing(String title, String type, Board board){
        this.title = title;
        this.type = ListingType.valueOf(type.toUpperCase());
        this.isArchived = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
        this.board = board;
        this.cards = new ArrayList<>();
    }
}
