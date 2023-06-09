package com.project.kanban.provider;

import com.project.kanban.auth.AuthException;
import com.project.kanban.user.UserCustomDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    CustomAuthenticationProvider(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserCustomDetail userDetails = (UserCustomDetail) userDetailsService.loadUserByUsername(
                username);
        if (userDetails == null)
            throw new AuthException.LoginFailed();
        return checkPassword(userDetails, password);
    }

    private Authentication checkPassword(UserCustomDetail userCustomDetail, String rawPassword) {
        if(passwordEncoder.matches(rawPassword, userCustomDetail.getUser().getPassword())) {
            log.info("Authentication with JWT is returned");
            return new UsernamePasswordAuthenticationToken(
                    userCustomDetail,
                    null);
        }
        else {
            log.error("Authentication failed");
            throw new AuthException.LoginFailed();
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }



}