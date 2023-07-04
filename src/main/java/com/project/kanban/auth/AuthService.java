package com.project.kanban.auth;

import com.project.kanban.jwt.JwtTokenProvider;
import com.project.kanban.payload.LoginRequest;
import com.project.kanban.user.UserCustomDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String returnToken(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate by both username and password

        final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );


            // If information is valid, set authentication to SecurityContext with authentication
            // On the other hand, the exception will be thrown.

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Return JWT to user
            return jwtTokenProvider.generateToken((UserCustomDetail) authentication.getPrincipal());
        }
        return "";

    }

    public static long getUserIdFromAuthentication(Authentication authentication){
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserCustomDetail) {
            return ((UserCustomDetail) principal).getUser().getId();
        }
        return 0;
    }

    public static boolean checkAuthority(String userRole, String requiredRole){
        return userRole.equals(requiredRole);
    }
}
