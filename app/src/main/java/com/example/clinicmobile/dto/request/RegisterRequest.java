package com.example.clinicmobile.dto.request;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String phone;
    private String password;

    public RegisterRequest(String fullName, String email,
                           String phone, String password) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
}
