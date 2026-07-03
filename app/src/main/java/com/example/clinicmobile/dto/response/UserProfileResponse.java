package com.example.clinicmobile.dto.response;

public class UserProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String dob;
    private String gender;
    private String address;
    private String cccd;
    private String bhyt;
    private String bloodType;
    private boolean profileCompleted;

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getCccd() { return cccd; }
    public String getBhyt() { return bhyt; }
    public String getBloodType() { return bloodType; }
    public boolean isProfileCompleted() { return profileCompleted; }
}
