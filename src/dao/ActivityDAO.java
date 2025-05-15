package dao;

import model.Activity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    private final Connection conn;

    public ActivityDAO(Connection conn) {
        this.conn = conn;
    }

    // TODO: Procedure Call & Timestamp auto update within sql.
    public void insertActivity(Activity activity) {
        String sql = "{CALL InsertActivity(?,?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());
            stmt.setInt(3, activity.getAppointmentId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Activity> getAllActivitiesByAppointmentId(int appointmentId) {
        List<Activity> activities = new ArrayList<>();
        String sql = "{CALL getAllActivitiesByAppointmentId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                activities.add(mapToResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }


    public void updateActivity(Activity activity) {
        String sql = "UPDATE activity SET title = ?, description = ? WHERE title = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, activity.getTitle());        // new title
            stmt.setString(2, activity.getDescription());  // new description
            stmt.setString(3, activity.getTitle());        // old title (used in WHERE)

            stmt.executeUpdate();  // use executeUpdate for UPDATE/INSERT/DELETE
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void deleteActivity(Activity activity) {
        String sql = "DELETE FROM activity WHERE activity_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, activity.getActivityId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Activity mapToResultSetToActivity(ResultSet rs) throws SQLException {
        return new Activity(
                rs.getInt("activity_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getTimestamp("timestamp"),
                rs.getInt("appointment_id")
        );
    }
}
