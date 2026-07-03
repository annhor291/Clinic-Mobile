package com.example.clinicmobile.model;

public class DateItem {
    private String dayOfWeek; // "T2", "T3"...
    private String dayOfMonth; // "20", "21"...
    private String fullDate; // "2026-06-20" để gọi API
    private boolean isSelected;

    public DateItem(String dayOfWeek, String dayOfMonth, String fullDate, boolean isSelected) {
        this.dayOfWeek = dayOfWeek;
        this.dayOfMonth = dayOfMonth;
        this.fullDate = fullDate;
        this.isSelected = isSelected;
    }

    public String getDayOfWeek() { return dayOfWeek; }
    public String getDayOfMonth() { return dayOfMonth; }
    public String getFullDate() { return fullDate; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
