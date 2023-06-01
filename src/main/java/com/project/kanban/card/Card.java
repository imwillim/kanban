package com.project.kanban.card;


import com.project.kanban.listing.Listing;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "card")
@Data
@NoArgsConstructor
public class Card {
    @Id
    @SequenceGenerator(name = "card_sequence", allocationSize = 1)
    @GeneratedValue(generator = "card_sequence", strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_archived")
    private boolean isArchived;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    private Listing listing;

    public Card(String title, String description, Listing listing){
        this.title = title;
        this.description = description;
        this.isArchived = false;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.listing = listing;
    }

}
