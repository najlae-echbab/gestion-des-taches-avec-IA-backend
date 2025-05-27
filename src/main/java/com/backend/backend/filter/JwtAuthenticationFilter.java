package com.backend.backend.filter;

import com.backend.backend.service.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();
        System.out.println("📥 Incoming request: " + requestPath);

        // Log the Authorization header
        final String authHeader = request.getHeader("Authorization");
        System.out.println("🔑 Authorization Header reçu : " + authHeader);

        if (requestPath.startsWith("/auth/register") || requestPath.startsWith("/auth/authenticate")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No JWT token found in Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUsername(jwt); // Extract the username from the JWT token
            System.out.println("🔍 Username extrait du token : " + username);
        } catch (Exception e) {
            System.out.println("❌ Invalid JWT token: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println("👤 UserDetails chargé depuis la base de données : " + userDetails.getUsername());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Authentication réussie pour l'utilisateur : " + username);
            } else {
                System.out.println("❌ Token JWT invalide pour l'utilisateur : " + username);
            }
        } else {
            System.out.println("⚠️ L'utilisateur est déjà authentifié ou le nom d'utilisateur est nul.");
        }

        filterChain.doFilter(request, response);
    }
}
