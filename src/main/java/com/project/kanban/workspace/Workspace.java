package com.project.kanban.workspace;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity(name = "workspace")
@Data
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "workspace_id_idx", columnList = "workspace_id")
})
public class Workspace {
    @Id
    @SequenceGenerator(name = "workspace_sequence", sequenceName = "workspace_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workspace_sequence")
    @Column(name = "workspace_id", nullable = false)
    private long id;

    @Size(min = 3, max = 50, message = "Title of board have a length from 3 to 50 characters.")
    @NotNull(message = "Title of workspace cannot be null")
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_at", nullable = false)
    private long updatedAt;


    @Column(name = "created_at", nullable = false)
    private long createdAt;


    public Workspace(String title, String description) {
        this.title = title;
        this.description = description;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
