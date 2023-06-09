package com.project.kanban.card;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.kanban.listing.Listing;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity(name = "card")
@Data
@NoArgsConstructor
public class Card {
    @Id
    @SequenceGenerator(name = "card_sequence", allocationSize = 1)
    @GeneratedValue(generator = "card_sequence", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "title")
    @Size(min = 3, max = 50, message = "Title of card have a length from 3 to 50 characters.")
    @NotNull(message = "Title of card must not be null")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_archived")
    private boolean isArchived;

    @Column(name = "updated_at")
    private long updatedAt;

    @Column(name = "created_at")
    private long createdAt;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "listing_id")
    private Listing listing;

    public Card(String title, String description, Listing listing){
        this.title = title;
        this.description = description;
        this.isArchived = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
        this.listing = listing;
    }

}
