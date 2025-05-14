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

    public int insertAppointment(Appointment appointment) {
        String sql = "{CALL InsertAppointment(?, ?, ?, ?, ?, ?, ?, ?)}";
        int generatedId = 0;

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getAppointmentDate()));
            stmt.setString(2, appointment.getStatus().toString());
            stmt.setInt(3, appointment.getDuration());
            stmt.setInt(4, appointment.getCaregiverID());
            stmt.setInt(5, appointment.getElderID());
            stmt.setInt(6, appointment.getServiceID());
            stmt.setDouble(7, appointment.getTotalCost());
            stmt.registerOutParameter(8, java.sql.Types.INTEGER);
            stmt.execute();
            generatedId = stmt.getInt(8);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
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
        String sql = "{CALL GetAllAppointmentsByCaregiverId(?)}";
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
        String sql = "{CALL GetAllAppointmentsByGuardianId(?)}";
        List<Appointment> appointments = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, guardianID);
            try (ResultSet rs = stmt.executeQuery()){
                System.out.println("there are " + rs.getFetchSize() + " appointments retrieved in the dao");
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


    public void updateAppointmentStatus(int id, Appointment.AppointmentStatus newStatus) {
        String sql = "UPDATE appointment SET status = ? WHERE appointment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus.toString());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        int appointmentID = rs.getInt("appointment_id");
        return new Appointment(
                appointmentID,
                rs.getTimestamp("appointmentDate").toLocalDateTime(),
                Appointment.AppointmentStatus.valueOf(rs.getString("status")),
                rs.getInt("duration"),
                rs.getTimestamp("createdDate").toLocalDateTime(),
                rs.getInt("caregiver_id"),
                rs.getInt("elder_id"),
                rs.getInt("service_id"),
                rs.getDouble("totalCost"),
                Appointment.PaymentStatus.valueOf(rs.getString("paymentStatus"))
        );
    }

    public void updateAppointmentPaymentStatus(int appointmentId, Appointment.PaymentStatus newStatus) {
        String sql = "{CALL UpdateAppointmentPaymentStatus(?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.setString(2, newStatus.name());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
