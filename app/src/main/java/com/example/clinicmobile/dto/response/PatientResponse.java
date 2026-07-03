package com.example.clinicmobile.dto.response;

public class PatientResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String dob;
    private String gender;
    private String address;
    private String cccd;
    private String bhyt;
    private String bloodType;

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getCccd() { return cccd; }
    public String getBhyt() { return bhyt; }
    public String getBloodType() { return bloodType; }
}
