package util;

import java.sql.Timestamp;

public class Activity {
    private int activityId;
    private String title;
    private String description;
    private Timestamp timeStamp;

    public Activity(int activityId, String title, String description, Timestamp timeStamp) {
        this.activityId = activityId;
        this.title = title;
        this.description = description;
        this.timeStamp = timeStamp;
    }

    public Activity() {
        this.activityId = 0;
        this.title = "";
        this.description = "";
        this.timeStamp = null;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}
