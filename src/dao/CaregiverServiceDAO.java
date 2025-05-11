package dao;

import model.Caregiver;
import model.CaregiverService;
import model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaregiverServiceDAO {

    private final Connection conn;

    public CaregiverServiceDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertCaregiverService(CaregiverService caregiverService) {
        String sql = "{CALL InsertCaregiverService(?, ?, ?, ?)}";

        System.out.println("caregiver id: " + caregiverService.getCaregiverId()); // for debugging purposes
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
        System.out.println("caregiver and service ids in the getCaregiverService: " + caregiverId + " " + serviceId);
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

    public List<Service> getAllServicesByCaregiverId(int caregiverId) {
        List<Service> services = new ArrayList<>();
        String sql = "{CALL GetAllServicesByCaregiverId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public Caregiver getCaregiverByServiceId(int serviceID) {
        String sql = "{CALL GetCaregiverByServiceId(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                mapResultSetToCaregiver(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Caregiver mapResultSetToCaregiver(ResultSet rs) throws SQLException {
        String certificationsString = rs.getString("certifications");
        List<String> certificationsList = (certificationsString != null && !certificationsString.isEmpty())
                ? Arrays.asList(certificationsString.split(","))
                : new ArrayList<>();

        Caregiver.Gender gender = null;
        String genderString = rs.getString("gender");
        if (genderString != null && !genderString.trim().isEmpty()) {
            try {
                gender = Caregiver.Gender.valueOf(genderString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized gender value from DB: " + genderString);
            }
        }

        Caregiver.BackgroundCheckStatus bgStatus = null;
        String bgStatusString = rs.getString("background_check_status");
        if (bgStatusString != null && !bgStatusString.trim().isEmpty()) {
            try {
                bgStatus = Caregiver.BackgroundCheckStatus.valueOf(bgStatusString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized background status value from DB: " + bgStatusString);
            }
        }

        Caregiver.MedicalClearanceStatus medStatus = null;
        String medStatusString = rs.getString("medical_clearance_status");
        if (medStatusString != null && !medStatusString.trim().isEmpty()) {
            try {
                medStatus = Caregiver.MedicalClearanceStatus.valueOf(medStatusString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized medical status value from DB: " + medStatusString);
            }
        }

        Caregiver.EmploymentType employmentType = null;
        String employmentTypeString = rs.getString("employment_type");
        if (employmentTypeString != null && !employmentTypeString.trim().isEmpty()) {
            try {
                employmentType = Caregiver.EmploymentType.valueOf(employmentTypeString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized employment type value from DB: " + employmentTypeString);
            }
        }


        return new Caregiver(
                rs.getInt("caregiver_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getTimestamp("date_of_birth") != null ? rs.getTimestamp("date_of_birth").toLocalDateTime() : null,
                gender,
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("address"),
                certificationsList,
                bgStatus,
                medStatus,
                rs.getString("availability_schedule"),
                employmentType
        );
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("category"),
                rs.getString("service_name"),
                rs.getDouble("price"));
    }
}