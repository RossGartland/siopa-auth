package com.siopa.siopa_auth.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Represents a login request.
 */
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank //Prevents null value.
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
