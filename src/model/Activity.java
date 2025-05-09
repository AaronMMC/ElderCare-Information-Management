package model;

import java.time.LocalDateTime;

public class Activity {
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private int appointmentId;


    // add new constructors if necessary
    public Activity(String title, String description, LocalDateTime timestamp, int appointmentId) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.appointmentId = appointmentId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
}
