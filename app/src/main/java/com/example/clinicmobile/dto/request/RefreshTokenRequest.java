package com.example.clinicmobile.dto.request;

public class RefreshTokenRequest {

    private String refreshToken;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
}
