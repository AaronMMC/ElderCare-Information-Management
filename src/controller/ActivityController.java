package controller;

import dao.ActivityDAO;
import model.Activity;

import java.sql.Connection;
import java.util.List;

public class ActivityController {

    private final ActivityDAO activityDAO;

    public ActivityController(Connection conn) {
        this.activityDAO = new ActivityDAO(conn);
    }

    public void addActivity(Activity activity) {
        activityDAO.insertActivity(activity);
    }

    public Activity getActivityById(int id) {
        return activityDAO.getActivityById(id);
    }

    public List<Activity> getAllActivities() {
        return activityDAO.getAllActivities();
    }

    public void updateActivity(Activity activity) {
        activityDAO.updateActivity(activity);
    }

    public void deleteActivity(int id) {
        activityDAO.deleteActivity(id);
    }
}
