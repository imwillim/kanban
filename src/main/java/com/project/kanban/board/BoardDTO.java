package com.project.kanban.board;

import com.project.kanban.listing.Listing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private long id;
    private String title;
    private String description;
    private boolean isRemoved;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private long workspaceId;
    private List<Listing> listings;
}
