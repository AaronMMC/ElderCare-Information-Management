package dao;

import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private final Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertAppointment(Appointment appointment){
            String sql = "{CALL InsertAppointment(?, ?}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setInt(2, appointment.getDuration());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Appointment getAppointmentById(int id) {
        String sql = "{CALL GetAppointmentById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "{CALL GetAllAppointments()}";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) {
        String sql = "{CALL UpdateAppointment(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, appointment.getAppointmentID());
            stmt.setString(2, appointment.getStatus().name());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        String sql = "{CALL DeleteAppointment(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("appointment_id"),
                rs.getTimestamp("appointment_date").toLocalDateTime(),
                Appointment.AppointmentStatus.valueOf(rs.getString("status")),
                rs.getInt("duration"),
                rs.getTimestamp("creation_date").toLocalDateTime());
    }
}