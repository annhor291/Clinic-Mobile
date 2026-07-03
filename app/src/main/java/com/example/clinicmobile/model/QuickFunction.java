package com.example.clinicmobile.model;

public class QuickFunction {

    private String title;
    private int iconResId; // emoji
    private String type; // định danh để xử lý click

    public QuickFunction(String title, int icon, String type) {
        this.title = title;
        this.iconResId = icon;
        this.type = type;
    }

    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
    public String getType() { return type; }
}
