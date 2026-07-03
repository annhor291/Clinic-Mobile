package com.example.clinicmobile.dto.response;

public class TimeSlotResponse {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long scheduleId;
    private String slotDate;
    private String startTime;
    private String endTime;
    private String status;

    public Long getId() { return id; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSlotDate() { return slotDate; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
}
