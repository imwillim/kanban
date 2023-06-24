package com.project.kanban.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class SuccessfulResponse {
    private final long timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
    private int status;
    private String message;
    private Object data;
}
