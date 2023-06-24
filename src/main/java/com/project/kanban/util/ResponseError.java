package com.project.kanban.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
class ResponseError {
    private LocalDateTime timestamp;
    private int status;
    private String message;
}
