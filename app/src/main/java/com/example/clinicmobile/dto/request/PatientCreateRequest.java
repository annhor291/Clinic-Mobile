package com.example.clinicmobile.dto.request;

public class PatientCreateRequest {

    private String fullName;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String bloodType;
    private String insuranceNumber;

    public PatientCreateRequest(String fullName, String phone, String gender,
                                String dateOfBirth, String address,
                                String bloodType, String insuranceNumber) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.bloodType = bloodType;
        this.insuranceNumber = insuranceNumber;
    }

    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public String getBloodType() { return bloodType; }
    public String getInsuranceNumber() { return insuranceNumber; }
}
