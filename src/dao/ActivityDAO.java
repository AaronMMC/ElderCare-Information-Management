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

    public void insertActivity(Activity activity){
        String sql = "{CALL InsertActivity(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, activity.getTitle());
            stmt.setString(2, activity.getDescription());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Activity getActivityById(int id){
        String sql = "{CALL GetActivityById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToActivity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Activity> getAllActivities(){
        List<Activity> activities = new ArrayList<>();
        String sql = "{CALL GetAllActivities()}";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                activities.add(mapResultSetToActivity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
    }

    public void updateActivity(Activity activity) {
        String sql = "{CALL UpdateActivity(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, activity.getActivityId());
            stmt.setString(2, activity.getTitle());
            stmt.setString(3, activity.getDescription());
            stmt.setTimestamp(4, activity.getTimeStamp());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteActivity(int id) {
        String sql = "{CALL DeleteActivity(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
        Activity activity = new Activity();
        activity.setActivityId(rs.getInt("activity_id"));
        activity.setTitle(rs.getString("title"));
        activity.setDescription(rs.getString("description"));
        activity.setTimeStamp(rs.getTimestamp("timestamp"));
        return activity;
    }
}