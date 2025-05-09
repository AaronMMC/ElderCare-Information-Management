package model;

import java.sql.Timestamp;

public class Activity {
    private int activityId;
    private String title;
    private String description;
    private Timestamp timestamp;
    private int appointmentId;

    public Activity() {
        this.activityId = -1;
        this.title = "";
        this.description = "";
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Activity(String title, String description, int appointmentId) {
        this.title = title;
        this.description = description;
        this.appointmentId = appointmentId;
    }

    public Activity(int activityId, String title, String description, Timestamp timestamp, int appointmentId) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.appointmentId = appointmentId;
    }

    public Activity(int activityId, String title, String description, Timestamp timestamp) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
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

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
