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

    public void insertAppointment(Appointment appointment) {
        String sql = "{CALL InsertAppointmentWithElders(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setInt(2, appointment.getDuration());
            stmt.setInt(3, appointment.getCaregiverID());
            stmt.setInt(4, appointment.getGuardianID());
            stmt.setString(5, appointment.getStatus().name());
            stmt.setTimestamp(6, Timestamp.valueOf(appointment.getCreatedDate()));
            stmt.setString(7, joinElderIDs(appointment.getElderIDs()));

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String joinElderIDs(List<Integer> elderIDs) {
        return elderIDs.stream()
                .map(String::valueOf)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
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

    public List<Appointment> getAllAppointmentsByCaregiver(int caregiverID) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "{CALL GetAllAppointmentsByCaregiver()}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiverID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public List<Appointment> getAllAppointmentsByGuardian(int guardianID) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "{CALL GetAllAppointmentsByGuardian()}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, guardianID);
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    appointments.add(mapResultSetToAppointment(rs));
                }
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
        int appointmentID = rs.getInt("appointment_id");
        Appointment appointment = new Appointment(
                appointmentID,
                rs.getTimestamp("appointment_date").toLocalDateTime(),
                Appointment.AppointmentStatus.valueOf(rs.getString("status")),
                rs.getInt("duration"),
                rs.getTimestamp("creation_date").toLocalDateTime(),
                rs.getInt("caregiver_id"),
                rs.getInt("guardian_id"),
                getElderIDsByAppointmentId(appointmentID),
                rs.getInt("payment_id")
        );
        return appointment;
    }

    private List<Integer> getElderIDsByAppointmentId(int appointmentID) throws SQLException {
        List<Integer> elderIDs = new ArrayList<>();
        String sql = "SELECT elder_id FROM appointment_elders WHERE appointment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    elderIDs.add(rs.getInt("elder_id"));
                }
            }
        }
        return elderIDs;
    }
}
