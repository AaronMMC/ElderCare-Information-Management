package dao;

import model.Admin;
import model.Caregiver;
import model.Guardian;

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
            stmt.setString(10, String.join(",", caregiver.getCertifications()));
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
                mapResultSetToCaregiver(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCaregiver(Caregiver caregiver) {
        checkUsername(caregiver);
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
            stmt.setString( 12, caregiver.getEmploymentType().name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkUsername(Caregiver caregiver) {
        AdminDAO adminDAO = new AdminDAO(conn);
        GuardianDAO guardianDAO = new GuardianDAO(conn);

        // Gather all existing usernames
        List<Caregiver> caregivers = getAllCaregivers();
        List<Admin> admins = adminDAO.getAllAdmin();
        List<Guardian> guardians = guardianDAO.getAllGuardians();

        // Check for duplicates across all roles
        boolean usernameExists = caregivers.stream().anyMatch(cg -> cg.getUsername().equalsIgnoreCase(caregiver.getUsername())) ||
                admins.stream().anyMatch(ad -> ad.getUsername().equalsIgnoreCase(caregiver.getUsername())) ||
                guardians.stream().anyMatch(gd -> gd.getUsername().equalsIgnoreCase(caregiver.getUsername()));

        if (usernameExists) {
            throw new RuntimeException("Username is already taken!");
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

    public Caregiver findByUsername(String username) {
        String sql = "{CALL FindCaregiverByUsername(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, username);
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