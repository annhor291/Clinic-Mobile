package com.example.clinicmobile.dto.request;

public class AppointmentRequest {

    private Long patientId;
    private Long timeSlotId;
    private String note;

    public AppointmentRequest(Long patientId, Long timeSlotId, String note) {
        this.patientId = patientId;
        this.timeSlotId = timeSlotId;
        this.note = note;
    }

    public Long getPatientId() { return patientId; }
    public Long getTimeSlotId() { return timeSlotId; }
    public String getNote() { return note; }
}
