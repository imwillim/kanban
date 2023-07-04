package com.project.kanban.listing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingDTO {
    private long id;
    private String title;
    private boolean isArchived;
    private int columnOrder;
    private long updatedAt;
    private long createdAt;
    private long boardId;

    public ListingDTO(String title, int columnOrder){
        this.title = title;
        this.isArchived = false;
        this.columnOrder = columnOrder;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
