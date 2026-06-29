package com.example.demo.util;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // SKIP LOGIN REQUEST
        if (request.getRequestURI().equals("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                System.out.println("USERNAME: " + username);

                if (username != null ) {

                    User user = userRepository.findByUsername(username).orElse(null);

if (user != null) {

   UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
    user.getUsername(),   // string principal (IMPORTANT)
    null,
    Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
);

SecurityContextHolder.getContext().setAuthentication(auth);

    System.out.println("------ JWT FILTER START ------");
System.out.println("PATH: " + request.getRequestURI());
System.out.println("HEADER: " + request.getHeader("Authorization"));
}
                }

            } catch (Exception e) {
                // DO NOTHING — just continue request
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}