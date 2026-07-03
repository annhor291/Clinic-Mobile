package com.example.clinicmobile.dto.response;

public class TimeSlotResponse {

    private Long id;
    private String date;
    private String startTime;
    private String endTime;
    private String status;

    public Long getId() { return id; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
}
