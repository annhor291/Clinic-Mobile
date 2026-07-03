package com.example.clinicmobile.model;

public class Doctor {
    private Long id;
    private String name;
    private String specialty;
    private double rating;
    private int experienceYears;
    private String description;
    public Doctor(Long id, String name, String specialty, double rating, int experienceYears, String description) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.rating = rating;
        this.experienceYears = experienceYears;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public double getRating() { return rating; }
    public int getExperienceYears() { return experienceYears; }
    public String getDescription() { return description; }
}
