package com.ymmo.ymmoapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    private final String secretRefreshKey;
    private final String secretAccessKey;

    private final long jwtRefreshExpiration;
    private final long jwtAccessExpiration;

    public JWTUtils(@Value("${jwt.refresh.secret.key}") String secretRefreshKey,
                    @Value("${jwt.access.secret.key}") String secretAccessKey,
                    @Value("${jwt.refresh.expiration}") String jwtRefreshExpiration,
                    @Value("${jwt.access.expiration}") String jwtAccessExpiration) {
        this.secretRefreshKey = secretRefreshKey;
        this.secretAccessKey = secretAccessKey;
        this.jwtRefreshExpiration = Long.parseLong(jwtRefreshExpiration);
        this.jwtAccessExpiration = Long.parseLong(jwtAccessExpiration);
    }

    public long getJwtRefreshExpiration() {
        return jwtRefreshExpiration;
    }

    public long getJwtAccessExpiration() {
        return jwtAccessExpiration;
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList());
        return buildToken(claims, userDetails.getUsername(), secretAccessKey, jwtAccessExpiration);
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        String username = extractUsername(token, secretAccessKey);
        return username.equals(userDetails.getUsername()) && !isExpired(token, secretAccessKey);
    }

    public String extractUsernameFromAccessToken(String token) {
        return extractUsername(token, secretAccessKey);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails.getUsername(), secretRefreshKey, jwtRefreshExpiration);
    }

    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        String username = extractUsername(token, secretRefreshKey);
        return username.equals(userDetails.getUsername()) && !isExpired(token, secretRefreshKey);
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractUsername(token, secretRefreshKey);
    }

    private String buildToken(Map<String, Object> extraClaims, String subject,
                              String secret, long expirationMs) {
        Date now = new Date();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(signingKey(secret))
                .compact();
    }

    private String extractUsername(String token, String secret) {
        return extractClaim(token, secret, Claims::getSubject);
    }

    private boolean isExpired(String token, String secret) {
        return extractClaim(token, secret, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, String secret, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey signingKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
