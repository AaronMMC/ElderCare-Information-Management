package model;

import util.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    private final Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertAppointment(Appointment appointment) throws SQLException {
        String sql = "{CALL InsertAppointment(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(2, appointment.getStatus());
            stmt.setInt(3, appointment.getDuration());
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getCreatedDate()));
            stmt.execute();
        }
    }

    public Appointment getAppointmentById(int id) throws SQLException {
        String sql = "{CALL GetAppointmentById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
            }
        }
        return null;
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "{CALL GetAllAppointments()}";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        String sql = "{CALL UpdateAppointment(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, appointment.getAppointmentID());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(3, appointment.getStatus());
            stmt.setInt(4, appointment.getDuration());
            stmt.setTimestamp(5, Timestamp.valueOf(appointment.getCreatedDate()));
            stmt.execute();
        }
    }

    public void deleteAppointment(int id) throws SQLException {
        String sql = "{CALL DeleteAppointment(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        appt.setAppointmentID(rs.getInt("appointment_id"));
        appt.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
        appt.setStatus(rs.getString("status"));
        appt.setDuration(rs.getInt("duration"));
        appt.setCreatedDate(rs.getTimestamp("creation_date").toLocalDateTime());
        return appt;
    }
}