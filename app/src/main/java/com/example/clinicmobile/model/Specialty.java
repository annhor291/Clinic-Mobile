package com.example.clinicmobile.model;

public class Specialty {
    private Long id;
    private String name;
    private String description;
    private String icon; // emoji tạm, sau thay bằng URL ảnh

    public Specialty(Long id, String name, String description, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
}
