package com.project.kanban.payload;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    private String message = "Login successfully.";
    private int statusCode = 200;
    private long expiredAt = 3600;
    private String accessToken;
    private final String tokenType = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
