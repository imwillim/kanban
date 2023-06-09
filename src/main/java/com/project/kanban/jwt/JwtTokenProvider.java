package com.project.kanban.jwt;

import com.project.kanban.user.User;
import com.project.kanban.user.UserCustomDetail;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    // JWT_SECRET for Server
    private static final String JWT_SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // Expiration time of jwt - 1 hour - 3600000
    private static final long JWT_EXPIRATION = 3600000L;


    // Generate JWT from UserDetail

    public String generateToken(UserCustomDetail userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        User user = userDetails.getUser();
        Key signingKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        // Generate JWT from username
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Take UserDetail (subject of JWT) from JWT
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes()).build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
            throw ex;
        } catch (ExpiredJwtException ex) {
            log.error(ex.getMessage());
            throw ex;
            // Throw the ResponseEntity to be caught and handled by the calling code
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
            throw ex;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(String.valueOf(SignatureAlgorithm.HS512));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Claims extractClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes()).build()
                        .parseClaimsJws(token).getBody();
    }
}

