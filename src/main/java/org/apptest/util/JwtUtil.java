package org.apptest.util;

import io.smallrye.jwt.build.Jwt;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

public class JwtUtil {

    public static String generateAccessToken(UUID userId, String email) {
        return Jwt.issuer("apptest")
                .upn(email)
                .groups(Set.of("user"))
                .claim("userId", userId.toString())
                .expiresIn(Duration.ofMinutes(15))
                .sign();
    }

    public static String generateRefreshToken(UUID userId) {
        return Jwt.issuer("apptest-refresh")
                .claim("userId", userId.toString())
                .expiresIn(Duration.ofDays(7))
                .sign();
    }
}
