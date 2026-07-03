package com.example.clinicmobile.dto.response;

public class AppointmentResponse {

    private Long id;
    private String bookingCode;
    private Long patientId;
    private String patientName;
    private String patientPhone;
    private Long doctorId;
    private String doctorName;
    private String specialtyName;
    private Long timeSlotId;
    private String appointmentDate;
    private String startTime;
    private String endTime;
    private String appointmentTime;
    private String status;
    private String note;
    private String cancellationReason;
    private String cancelledAt;
    private String cancelledBy;
    private String createdAt;
    private String updatedAt;

    public Long getId() { return id; }
    public String getBookingCode() { return bookingCode; }
    public Long getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public String getPatientPhone() { return patientPhone; }
    public Long getDoctorId() { return doctorId; }
    public String getDoctorName() { return doctorName; }
    public String getSpecialtyName() { return specialtyName; }
    public Long getTimeSlotId() { return timeSlotId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getStatus() { return status; }
    public String getNote() { return note; }
    public String getCancellationReason() { return cancellationReason; }
    public String getCreatedAt() { return createdAt; }
}
