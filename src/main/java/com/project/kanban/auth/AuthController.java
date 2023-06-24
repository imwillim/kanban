package com.project.kanban.auth;

import com.project.kanban.payload.LoginRequest;
import com.project.kanban.payload.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1")
public class AuthController {
    private final AuthService authService;
    @PostMapping(path = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(new LoginResponse(authService.returnToken(loginRequest)));
    }
}
