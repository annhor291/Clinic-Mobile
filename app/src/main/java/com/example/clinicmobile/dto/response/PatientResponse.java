package com.example.clinicmobile.dto.response;

public class PatientResponse {

    private Long id;
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String bloodType;
    private String allergies;
    private String medicalNotes;
    private String insuranceNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String createdAt;
    private String updatedAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public String getBloodType() { return bloodType; }
    public String getAllergies() { return allergies; }
    public String getMedicalNotes() { return medicalNotes; }
    public String getInsuranceNumber() { return insuranceNumber; }
    public String getEmergencyContactName() { return emergencyContactName; }
    public String getEmergencyContactPhone() { return emergencyContactPhone; }
}
