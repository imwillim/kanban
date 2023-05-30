package com.project.kanban.workspace;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkspaceDTO {
    private long id;
    private String title;
    private String description;


}
