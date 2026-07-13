package com.jwt.AegisAuth.filter;

import com.jwt.AegisAuth.entity.UserEntity;
import com.jwt.AegisAuth.exception.ResourceNotFoundException;
import com.jwt.AegisAuth.repository.UserRepository;
import com.jwt.AegisAuth.service.BlacklistService;
import com.jwt.AegisAuth.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final BlacklistService blacklistService;

    public JWTFilter(JWTService jwtService, UserRepository userRepository, BlacklistService blacklistService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")){
           filterChain.doFilter(request,response);
           return;
        }

        String jwtToken = authorization.substring(7);

        // Check blacklist
        if (blacklistService.isBlacklisted(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    "error": "Token has been logged out"
                }
                """);
            return;
        }

        String username;

        try {
            username = jwtService.getUsername(jwtToken);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
            return;
        }

        if (username == null){
            filterChain.doFilter(request,response);
           return;
       }

        UserEntity userData = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

       UserDetails userDetails = User.builder()
                .username(userData.getUsername())
                .password(userData.getPassword())
                .authorities("ROLE_" + userData.getRole())
                .build();

        System.out.println("Authorities: " + userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
               userDetails,
               null,
               userDetails.getAuthorities());

       token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

       SecurityContextHolder.getContext().setAuthentication(token);

       filterChain.doFilter(request,response);
    }
}
