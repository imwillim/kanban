package com.project.kanban.listing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ListingDTO {
    private long id;
    private String title;
    private String type;
    private boolean isArchived;
    private long updatedAt;
    private long createdAt;
    private long boardId;

    public ListingDTO(String title){
        this.title = title;
        this.type = "TODO";
        this.isArchived = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
