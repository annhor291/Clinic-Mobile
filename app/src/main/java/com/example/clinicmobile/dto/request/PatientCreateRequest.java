package com.example.clinicmobile.dto.request;

public class PatientCreateRequest {

    private String fullName;
    private String phone;
    private String dob;
    private String gender;
    private String address;
    private String cccd;
    private String bhyt;
    private String bloodType;

    public PatientCreateRequest(String fullName, String phone, String dob,
                                String gender, String address, String cccd,
                                String bhyt, String bloodType) {
        this.fullName = fullName;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.cccd = cccd;
        this.bhyt = bhyt;
        this.bloodType = bloodType;
    }

    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public String getCccd() { return cccd; }
    public String getBhyt() { return bhyt; }
    public String getBloodType() { return bloodType; }
}
