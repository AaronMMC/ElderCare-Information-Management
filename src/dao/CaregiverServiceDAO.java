package dao;

import model.CaregiverService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaregiverServiceDAO {

    private final Connection conn;

    public CaregiverServiceDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertCaregiverService(CaregiverService caregiverService) {
        String sql = "{CALL InsertCaregiverService(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiverService.getCaregiverId());
            stmt.setInt(2, caregiverService.getServiceId());
            stmt.setInt(3, caregiverService.getExperienceYears());
            stmt.setDouble(4, caregiverService.getHourlyRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CaregiverService getCaregiverService(int caregiverId, int serviceId) {
        String sql = "{CALL GetCaregiverService(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiverId);
            stmt.setInt(2, serviceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int experienceYears = rs.getInt("experience_years");
                double hourlyRate = rs.getDouble("hourly_rate");
                return new CaregiverService(caregiverId, serviceId, experienceYears, hourlyRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCaregiverService(CaregiverService caregiverService) {
        String sql = "{CALL UpdateCaregiverService(?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiverService.getCaregiverId());
            stmt.setInt(2, caregiverService.getServiceId());
            stmt.setInt(3, caregiverService.getExperienceYears());
            stmt.setDouble(4, caregiverService.getHourlyRate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCaregiverService(int caregiverId, int serviceId) {
        String sql = "{CALL DeleteCaregiverService(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiverId);
            stmt.setInt(2, serviceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CaregiverService> getAllCaregiverServices() {
        List<CaregiverService> services = new ArrayList<>();
        String sql = "{CALL GetAllCaregiverServices()}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int caregiverId = rs.getInt("caregiver_id");
                int serviceId = rs.getInt("service_id");
                int experienceYears = rs.getInt("experience_years");
                double hourlyRate = rs.getDouble("hourly_rate");

                CaregiverService caregiverService = new CaregiverService(caregiverId, serviceId, experienceYears, hourlyRate);
                services.add(caregiverService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }
}