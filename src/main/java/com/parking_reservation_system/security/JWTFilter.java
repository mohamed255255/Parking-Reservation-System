package com.parking_reservation_system.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.parking_reservation_system.service.CustomUserDetailsService;
import com.parking_reservation_system.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends  OncePerRequestFilter {


    private final JWTService jwtService;
    private final CustomUserDetailsService userService;

    @Autowired
    public JWTFilter(JWTService jwtService,CustomUserDetailsService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain)
                throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String userName = jwtService.extractUserName(token);

                // Check if username is present AND no authentication exists yet
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = userService.loadUserByUsername(userName);

                    if (jwtService.validateToken(token, user)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        user, null, user.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        }
    }


