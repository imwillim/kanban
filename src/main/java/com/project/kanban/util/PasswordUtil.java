package com.project.kanban.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    // BCrypt encoded
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean matches(String password, String encodedPassword){
        return passwordEncoder.matches(password, encodedPassword);
    }
}
