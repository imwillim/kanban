package com.project.kanban.workspace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    private long id;
    private String title;
    private String description;
    private long updatedAt;
    private long createdAt;

    public WorkspaceDTO(String title, String description){
        this.title = title;
        this.description = description;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
