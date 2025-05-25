package org.apptest.response;

import java.util.UUID;

public class LoginResponse {
    public UUID userId;
    public String email;
    public String message;
    public String accessToken;
    public String refreshToken;

    public LoginResponse(UUID userId, String email, String message, String accessToken, String refreshToken) {
        this.userId = userId;
        this.email = email;
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
