package controller;

import dao.ActivityDAO;
import model.Activity;
import model.Appointment;

import java.sql.Connection;
import java.util.List;

public class ActivityController {

    private final ActivityDAO activityDAO;

    public ActivityController(Connection conn) {
        this.activityDAO = new ActivityDAO(conn);
    }

    public void addActivity(Activity activity) {
        this.activityDAO.insertActivity(activity);
    }

    public List<Activity> getAllActivitiesByAppointment(Appointment appointment) {
        return activityDAO.getAllActivitiesByAppointmentId(appointment.getAppointmentID());
    }

    public void updateActivity(Activity activity) {
        this.activityDAO.updateActivity(activity);
    }

    public void deleteActivity(Activity activity) {
        this.activityDAO.deleteActivity(activity);
    }
}
