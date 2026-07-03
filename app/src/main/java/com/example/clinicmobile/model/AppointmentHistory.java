package com.example.clinicmobile.model;

public class AppointmentHistory {

    private String doctorName;
    private String specialty;
    private String date;
    private String time;
    private String status;

    public AppointmentHistory(
            String doctorName,
            String specialty,
            String date,
            String time,
            String status) {

        this.doctorName = doctorName;
        this.specialty = specialty;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
