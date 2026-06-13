package com.ymmo.ymmoapi.config;

import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.security.HeaderValidationFilter;
import com.ymmo.ymmoapi.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] PUBLIC_ROUTES = {
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/refresh",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/"
    };

    private final UsersRepository usersRepository;
    private final JWTUtils jwtUtils;

    @Autowired
    public SecurityConfiguration(UsersRepository usersRepository, JWTUtils jwtUtils) {
        this.usersRepository = usersRepository;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ROUTES).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler())
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new HeaderValidationFilter(jwtUtils, usersRepository), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            System.out.println("=== ACCESS DENIED ===");
            System.out.println("Request: " + request.getMethod() + " " + request.getRequestURI());
            System.out.println("Exception: " + accessDeniedException.getMessage());
            System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }
            accessDeniedException.printStackTrace();
            response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: Add specific origin for the cors config
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : " + email));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
