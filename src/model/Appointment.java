package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private int caregiverID;
    private int guardianID;
    private List<Integer> elderIDs;

    public Appointment(int appointmentID, LocalDateTime appointmentDate, AppointmentStatus status, int duration, LocalDateTime createdDate, int caregiverID, int guardianID, List<Integer> elderIDs) {
        this.appointmentID = appointmentID;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.duration = duration;
        this.createdDate = createdDate;
        this.caregiverID = caregiverID;
        this.guardianID = guardianID;
        this.elderIDs = elderIDs != null ? elderIDs : new ArrayList<>();
    }

    public Appointment(int appointmentID, LocalDateTime appointmentDate, AppointmentStatus status, int duration, LocalDateTime createdDate) {
        this(appointmentID, appointmentDate, status, duration, createdDate, 0, 0, new ArrayList<>());
    }

    public Appointment() {
        this(0, null, AppointmentStatus.UNPAID, 0, null, 0, 0, new ArrayList<>());
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

    public int getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(int guardianID) {
        this.guardianID = guardianID;
    }

    public List<Integer> getElderIDs() {
        return elderIDs;
    }

    public void setElderIDs(List<Integer> elderIDs) {
        this.elderIDs = elderIDs != null ? elderIDs : new ArrayList<>();
    }
}