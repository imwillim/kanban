package com.project.kanban.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtFilterException {
    private long timestamp = Timestamp.from(Instant.now()).getTime();
    private int status = HttpStatus.UNAUTHORIZED.value();
    private String message;
    public JwtFilterException(String message){
        this.message = message;
    }
}
