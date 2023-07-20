package com.project.kanban.board;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.kanban.listing.Listing;
import com.project.kanban.workspace.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@Data
@Table(indexes = {
        @Index(name = "board_id_idx", columnList = "board_id")
})
@NoArgsConstructor
public class Board {
    @Id
    @SequenceGenerator(name = "board_sequence", sequenceName = "board_sequence", allocationSize = 1)
    @GeneratedValue(generator = "board_sequence", strategy = GenerationType.SEQUENCE)
    @Column(name = "board_id")
    private long id;

    @Column(name = "title")
    @Size(min = 3, max = 50, message = "Title of board have a length from 3 to 50 characters.")
    @NotNull(message = "Title of board must not be null.")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @Column(name = "updated_at")
    private long updatedAt;

    @Column(name = "created_at")
    private long createdAt;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board") // Should have mappedBy
    @JsonBackReference
    private List<Listing> listings;

    public Board(String title, String description, Workspace workspace){
        this.title = title;
        this.description = description;
        this.isRemoved = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
        this.workspace = workspace;
        listings = new ArrayList<>();
    }



}
