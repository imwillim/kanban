package com.project.kanban.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.project.kanban.auth.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
/*
The doFilterInternal method intercepts the requests then checks the Authorization header.
If the header is not present or doesn’t start with “BEARER”, it proceeds to the filter chain.

If the header is present, the getAuthentication method is invoked.
getAuthentication verifies the JWT, and if the token is valid, it returns an access token which Spring will use internally.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService){
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request,
                                        @NonNull HttpServletResponse response,
                                        @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // Take jwt from request
            String jwt = getJwtFromRequest(request);
            String path = request.getRequestURI();

            if (path.equals("/api/v1/login") || path.equals("/api/v1/signup")) {
                filterChain.doFilter(request, response);
                return;
            }


            /* if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) { */
            if (jwtTokenProvider.validateToken(jwt)) {
                // Take id user from JWT
                String email = jwtTokenProvider.getEmailFromJWT(jwt);
                // Take user detail (payload) from id
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if(userDetails != null) {
                    // If user is valid, set User to Security Context
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {

            /* Keep in mind that OncePerRequestFilter is not directly designed to return a ResponseEntity
             since it operates at a lower level in the servlet filter chain.
              However, you can still send an error response using the HttpServletResponse object.*/
            /* => RUNTIME EXCEPTION */

            // Set the response status and content type
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            // Write the error response to the response body
            ObjectMapper objectMapper = new ObjectMapper();
            String errorResponseJson = objectMapper.writeValueAsString(new JwtFilterException(ex.getMessage()));

            response.getWriter().write(errorResponseJson);
            // Return from the doFilterInternal method
            return;

        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Check if header Authorization contains JWT
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return "";
    }

}
