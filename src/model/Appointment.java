package model;

import java.time.LocalDateTime;

public class Appointment {

    public enum AppointmentStatus {
        PAID,
        UNPAID,
        FINISHED,
        CANCELLED
    }

    private int appointmentID;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private int duration;
    private LocalDateTime createdDate;

    public Appointment(int appointmentID, LocalDateTime appointmentDate, AppointmentStatus status, int duration, LocalDateTime createdDate) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.duration = duration;
        this.createdDate = createdDate;
    }

    public Appointment(){
        this.appointmentID = 0;
        this.appointmentDate = null;
        this.status = AppointmentStatus.UNPAID;
        this.duration = 0;
        this.createdDate = null;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}