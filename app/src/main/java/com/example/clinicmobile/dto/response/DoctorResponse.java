package com.example.clinicmobile.dto.response;

public class DoctorResponse {

    private Long id;
    private Long userId;
    private String username;
    private String email;
    private Long specialtyId;
    private String specialtyName;
    private String fullName;
    private String title;
    private Integer experienceYears;
    private String phone;
    private String bio;
    private Double consultationFee;
    private boolean active;
    private String createdAt;
    private String updatedAt;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Long getSpecialtyId() { return specialtyId; }
    public String getSpecialtyName() { return specialtyName; }
    public String getFullName() { return fullName; }
    public String getTitle() { return title; }
    public Integer getExperienceYears() { return experienceYears; }
    public String getPhone() { return phone; }
    public String getBio() { return bio; }
    public Double getConsultationFee() { return consultationFee; }
    public boolean isActive() { return active; }
}
