package com.levelup.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.levelup.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
    
    @Autowired
    private JwtUtil util;

    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // Skip filter for auth endpoints (login/register) and H2 console
        if (path.startsWith("/api/auth") || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                email = util.obtenerCorreo(jwt);
            } catch (Exception e) {
                logger.warn("Error obteniendo email del token ", e);
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // validate token first; avoid loading user if token invalid
            if (jwt != null && util.validacionToken(jwt)) {
                try {
                    UserDetails details = this.service.loadUserByUsername(email);
                    if (details != null && email.equals(details.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception ex) {
                    // user not found or other issue - log and continue without auth
                    logger.warn("User not found or error loading user for email from token: " + email);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
