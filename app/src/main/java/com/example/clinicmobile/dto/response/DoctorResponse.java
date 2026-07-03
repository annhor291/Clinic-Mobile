package com.example.clinicmobile.dto.response;

public class DoctorResponse {

    private Long id;
    private String fullName;
    private String specialty;
    private double rating;
    private int experienceYears;
    private String description;
    private String avatarUrl;

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getSpecialty() { return specialty; }
    public double getRating() { return rating; }
    public int getExperienceYears() { return experienceYears; }
    public String getDescription() { return description; }
    public String getAvatarUrl() { return avatarUrl; }
}
