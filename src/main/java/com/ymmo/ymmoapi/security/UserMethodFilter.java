package com.ymmo.ymmoapi.security;

import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class UserMethodFilter extends OncePerRequestFilter {
    private final JWTUtils jwtUtils;
    private final UsersRepository usersRepository;

    private static final List<String> UNAFFECTED_ROUTES = List.of(
            "/api/auth/",
            "/api/transactions/",
            "/api/property/"
    );

    @Autowired
    public UserMethodFilter(JWTUtils jwtUtils, UsersRepository usersRepository) {
        this.jwtUtils = jwtUtils;
        this.usersRepository = usersRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.equals("/")) return true;
        return UNAFFECTED_ROUTES.stream()
                .anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (HttpMethod.POST.matches(request.getMethod()) || HttpMethod.PATCH.matches(request.getMethod()) || HttpMethod.DELETE.matches(request.getMethod())) {
                String token = request.getHeader("Authorization").substring(7);
                String email = jwtUtils.extractUsernameFromAccessToken(token);
                Users user = usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));
                if (user.getAuthorities().stream().filter(Objects::nonNull).noneMatch(value -> Objects.equals(value.getAuthority(), "ROLE_admin"))) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
