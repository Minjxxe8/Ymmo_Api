package com.ymmo.ymmoapi.dto;

public class UserAuthDto {
    public record LoginRequest(
            String email,
            String password
    ) {
    }

    public record RefreshRequest(
            String refreshToken
    ) {
    }

    public record LogoutAll(
            String refreshToken,
            boolean revokeAll
    ) {
    }

    public record AuthResponse(
            String accessToken,
            String refreshToken,
            String tokenType,
            long accessExpiresIn,
            long refreshExpiresIn
    ) {
        public static AuthResponse of(String accessToken, String refreshToken,
                                      long accessMs, long refreshMs) {
            return new AuthResponse(accessToken, refreshToken, "Bearer",
                    accessMs / 1000, refreshMs / 1000);
        }
    }
}
