package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment {

    public Appointment() {

    }

    public enum AppointmentStatus {
        FINISHED,
        CANCELLED,
        ONGOING,
        PENDING
    }

    private int appointmentID;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private int duration;
    private LocalDateTime createdDate;
    private int caregiverID;
    private int elderID;
    private int serviceID;

    public Appointment(int appointmentID, LocalDateTime appointmentDate, AppointmentStatus status, int duration, LocalDateTime createdDate, int caregiverID, int elderID, int serviceID) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.duration = duration;
        this.createdDate = createdDate;
        this.caregiverID = caregiverID;
        this.elderID = elderID;
        this.serviceID = serviceID;
    }


    public Appointment(LocalDateTime appointmentDate, AppointmentStatus status, int duration, LocalDateTime createdDate, int caregiverID, int paymentID, int elderID, int serviceID) {
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.duration = duration;
        this.createdDate = createdDate;
        this.caregiverID = caregiverID;
        this.elderID = elderID;
        this.serviceID = serviceID;
    }


    public Appointment(LocalDateTime appointmentDate, AppointmentStatus status, int duration, int caregiverID, int elderID, int serviceID) {
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.duration = duration;
        this.caregiverID = caregiverID;
        this.elderID = elderID;
        this.serviceID = serviceID;
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

    public int getCaregiverID() {
        return caregiverID;
    }

    public void setCaregiverID(int caregiverID) {
        this.caregiverID = caregiverID;
    }

    public int getElderID() {
        return elderID;
    }

    public void setElderID(int elderID) {
        this.elderID = elderID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }
}