package util;

import java.time.LocalDateTime;

public class Activity {
    private int activityId;
    private String title;
    private String description;
    private LocalDateTime timeStamp;

    public Activity(int activityId, String title, String description, LocalDateTime timeStamp) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
