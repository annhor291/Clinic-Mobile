package com.example.clinicmobile.model;

public class TimeSlot {

    private Long id;

    private String slotDate;

    private String startTime;

    private String endTime;

    private String status;

    public TimeSlot(
            Long id,
            String slotDate,
            String startTime,
            String endTime,
            String status) {

        this.id = id;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }
}
