package com.siopa.siopa_auth.payload.response;

import java.util.List;
import java.util.UUID;

/**
 * Represents a JWT object that is returned.
 */
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UUID userId;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, UUID userId, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public UUID getId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}