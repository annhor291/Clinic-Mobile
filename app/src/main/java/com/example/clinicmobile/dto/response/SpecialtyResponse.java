package com.example.clinicmobile.dto.response;

public class SpecialtyResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private Integer totalDoctors;
    private String createdAt;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isActive() { return active; }
    public Integer getTotalDoctors() { return totalDoctors; }
}
