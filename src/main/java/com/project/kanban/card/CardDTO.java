package com.project.kanban.card;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
public class CardDTO {
    private long id;
    private String title;
    private String description;
    private boolean isArchived;
    private long updatedAt;
    private long createdAt;
    private long listingId;

    public CardDTO(String title){
        this.title = title;
        this.isArchived = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
