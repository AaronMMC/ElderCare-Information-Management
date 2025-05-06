package dao;

import model.Caregiver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaregiverDAO {

    private final Connection conn;

    public CaregiverDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertCaregiver(Caregiver caregiver) {
        String sql = "{CALL InsertCaregiver(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, caregiver.getUsername());
            stmt.setString(2, caregiver.getPassword());
            stmt.setString(3, caregiver.getFirstName());
            stmt.setString(4, caregiver.getLastName());
            stmt.setTimestamp(5, Timestamp.valueOf(caregiver.getDateOfBirth()));
            stmt.setString(6, caregiver.getGender().name());
            stmt.setString(7, caregiver.getContactNumber());
            stmt.setString(8, caregiver.getEmail());
            stmt.setString(9, caregiver.getAddress());
            stmt.setString(10, String.join(",", caregiver.getCertifications()));;
            stmt.setString(11, caregiver.getEmploymentType().name());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Caregiver> getAllCaregivers() {
        List<Caregiver> caregivers = new ArrayList<>();
        String sql = "{CALL GetAllCaregivers()}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
           ResultSet rs = stmt.executeQuery();
           while (rs.next()) {
               caregivers.add(mapResultSetToCaregiver(rs));
           }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caregivers;
    }

    public Caregiver getCaregiverById(int id) {
        String sql = "{CALL GetCaregiverById(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Caregiver caregiver = new Caregiver();
                caregiver.setCaregiverID(rs.getInt("caregiver_id"));
                caregiver.setUsername(rs.getString("username"));
                caregiver.setPassword(rs.getString("password"));
                caregiver.setFirstName(rs.getString("first_name"));
                caregiver.setLastName(rs.getString("last_name"));
                caregiver.setDateOfBirth(rs.getTimestamp("date_of_birth").toLocalDateTime());
                caregiver.setGender(Caregiver.Gender.valueOf(rs.getString("gender")));
                caregiver.setContactNumber(rs.getString("contact_number"));
                caregiver.setEmail(rs.getString("email"));
                caregiver.setAddress(rs.getString("address"));
                caregiver.setCertifications(Arrays.asList(rs.getString("certifications").split(",")));
                caregiver.setBackgroundCheckStatus(Caregiver.BackgroundCheckStatus.valueOf(rs.getString("background_check_status")));
                caregiver.setMedicalClearanceStatus(Caregiver.MedicalClearanceStatus.valueOf(rs.getString("medical_clearance_status")));
                caregiver.setAvailabilitySchedule(rs.getString("availability_schedule"));
                caregiver.setEmploymentType(Caregiver.EmploymentType.valueOf(rs.getString("employment_type")));
                return caregiver;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

        public void updateCaregiver(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiver(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiver.getCaregiverID());
            stmt.setString(2, caregiver.getUsername());
            stmt.setString(3, caregiver.getPassword());
            stmt.setString(4, caregiver.getFirstName());
            stmt.setString(5, caregiver.getLastName());
            stmt.setTimestamp(6, Timestamp.valueOf(caregiver.getDateOfBirth()));
            stmt.setString(7, caregiver.getGender().name());
            stmt.setString(8, caregiver.getContactNumber());
            stmt.setString(9, caregiver.getEmail());
            stmt.setString(10, caregiver.getAddress());
            stmt.setString(11, String.join(",", caregiver.getCertifications()));
            stmt.setString(12, caregiver.getEmploymentType().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCaregiverSchedule(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiverSchedule(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, caregiver.getCaregiverID());
            stmt.setString(2, caregiver.getAvailabilitySchedule());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCaregiverBackgroundStatus(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiverBackgroundStatus(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiver.getCaregiverID());
            stmt.setString(2, caregiver.getBackgroundCheckStatus().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCaregiverMedicalClearanceStatus(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiverMedicalClearanceStatus(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiver.getCaregiverID());
            stmt.setString(2, caregiver.getMedicalClearanceStatus().name());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCaregiver(int id) {
        String sql = "{CALL DeleteCaregiver(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Caregiver findByUsernameAndPassword(String username, String password) {
        String sql = "{CALL FindCaregiverByUsernameAndPassword(?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCaregiver(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Caregiver mapResultSetToCaregiver(ResultSet rs) throws SQLException {
        return new Caregiver(
                rs.getInt("caregiver_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getTimestamp("date_of_birth").toLocalDateTime(),
                Caregiver.Gender.valueOf(rs.getString("gender")),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("address"),
                List.of(rs.getString("certifications").split(",")),
                Caregiver.BackgroundCheckStatus.valueOf(rs.getString("background_check_status")),
                Caregiver.MedicalClearanceStatus.valueOf(rs.getString("medical_clearance_status")),
                rs.getString("availability_schedule"),
                Caregiver.EmploymentType.valueOf(rs.getString("employment_type"))
        );
    }
}