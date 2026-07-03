package com.example.clinicmobile.dto.response;

public class AuthResponse {

    private String token;
    private String refreshToken;
    private Long userId;
    private String username;
    private String email;
    private String role;
    private Long profileId;

    public String getToken() { return token; }
    public String getRefreshToken() { return refreshToken; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getProfileId() { return profileId; }
}
