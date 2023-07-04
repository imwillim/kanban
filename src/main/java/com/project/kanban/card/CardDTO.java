package com.project.kanban.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private long id;
    private String title;
    private String description;
    private boolean isArchived;
    private int columnOrder;
    private int rowOrder;
    private long updatedAt;
    private long createdAt;
    private long listingId;

    public CardDTO(String title, int columnOrder, int rowOrder){
        this.title = title;
        this.columnOrder = columnOrder;
        this.rowOrder = rowOrder;
        this.isArchived = false;
        this.updatedAt = Timestamp.from(Instant.now()).getTime();
        this.createdAt = Timestamp.from(Instant.now()).getTime();
    }
}
