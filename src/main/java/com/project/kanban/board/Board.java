package com.project.kanban.board;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.kanban.listing.Listing;
import com.project.kanban.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Data
@NoArgsConstructor
public class Board {
    @Id
    @SequenceGenerator(name = "board_sequence", sequenceName = "board_sequence", allocationSize = 1)
    @GeneratedValue(generator = "board_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinTable
    private List<Listing> listings;

    public Board(String title, String description, Workspace workspace){
        this.title = title;
        this.description = description;
        this.isRemoved = false;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.workspace = workspace;
        listings = new ArrayList<>();
    }

}
