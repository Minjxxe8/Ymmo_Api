package com.ymmo.ymmoapi.security;

import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class HeaderValidationFilter extends OncePerRequestFilter {
    JWTUtils jwtUtils;
    UsersRepository usersRepository;

    private static final List<String> PUBLIC_ROUTES = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/refresh",
            "/"
    );

    public HeaderValidationFilter(JWTUtils jwtUtils, UsersRepository usersRepository) {
        this.jwtUtils = jwtUtils;
        this.usersRepository = usersRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return PUBLIC_ROUTES.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String jwt = authHeader.substring(7);
                String email = jwtUtils.extractUsernameFromAccessToken(jwt);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Users user = usersRepository.findByEmail(email)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));
                    System.out.println("Is token valid ? " + jwtUtils.validateAccessToken(jwt, user));
                    if (jwtUtils.validateAccessToken(jwt, user)) {
                        System.out.println("Authorities : " + user.getAuthorities());
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        var context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authenticationToken);
                        SecurityContextHolder.setContext(context);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Forbidden on Header Validation Exception");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.out.println("Forbidden on Header Validation");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
