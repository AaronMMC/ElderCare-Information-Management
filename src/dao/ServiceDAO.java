package dao;

import model.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private final Connection conn;

    public ServiceDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertService(Service service) {
        try {
            CallableStatement stmt = conn.prepareCall("{call insert_service(?, ?, ?)}");
            stmt.setString(1, service.getCategory());
            stmt.setString(2, service.getServiceName());
            stmt.setDouble(3, service.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Service getServiceByID(int serviceID) {
        String sql = "SELECT * FROM service WHERE service_id = ?";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, serviceID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToService(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Service> getAllServices() {
        String sql = "Select * FROM service";
        List<Service> services = new ArrayList<>();
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public List<Service> getAllServicesByAppointmentId(int appointmentID) {
        String sql = "SELECT s.*\n" +
                "    FROM service s\n" +
                "    INNER JOIN caregiverservice cs ON s.service_id = cs.service_id\n" +
                "    INNER JOIN appointment a ON cs.caregiver_id = a.caregiver_id\n" +
                "    WHERE a.appointment_id = ?";
        List<Service> services = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1,appointmentID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
    ///Incorrect number of arguments for PROCEDURE lalakers2.InsertElder; expected 7, got 6
    public List<Service> getAllServicesByCaregiverId(int caregiverId) {
        String sql = "  SELECT s.*\n" +
                "    FROM service s\n" +
                "    INNER JOIN caregiverservice cs ON s.service_id = cs.service_id\n" +
                "    INNER JOIN caregiver c ON cs.caregiver_id = c.caregiver_id\n" +
                "    WHERE c.caregiver_id = ?";
        List<Service> services = new ArrayList<>();
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiverId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;

    }

    public void updateService(Service service) {
        try {
            CallableStatement stmt = conn.prepareCall("{call update_service(?, ?, ?, ?)}");
            stmt.setInt(1, service.getServiceID());
            stmt.setString(2, service.getCategory());
            stmt.setString(3, service.getServiceName());
            stmt.setDouble(4, service.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteService(int serviceID) {
        String sql = "DELETE FROM service \n" +
                "    WHERE service_id = ?";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, serviceID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("category"),
                rs.getString("service_name"),
                rs.getDouble("price"));
    }
}