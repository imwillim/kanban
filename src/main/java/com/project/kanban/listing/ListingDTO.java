package com.project.kanban.listing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ListingDTO {
    private long id;
    private String title;
    private String type;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private long boardId;

    public ListingDTO(String title){
        this.title = title;
        this.type = "Todo";
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
}
