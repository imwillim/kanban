package com.project.kanban.card;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardDTO {
    private long id;
    private String title;
    private String description;
    private boolean isArchived;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private long listingId;
}
