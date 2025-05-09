package dao;

import model.Admin;
import model.Caregiver;
import model.Guardian;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
            if (caregiver.getDateOfBirth() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(caregiver.getDateOfBirth()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            if (caregiver.getGender() != null) {
                stmt.setString(6, caregiver.getGender().name());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }
            stmt.setString(7, caregiver.getContactNumber());
            stmt.setString(8, caregiver.getEmail());
            stmt.setString(9, caregiver.getAddress());
            if (caregiver.getCertifications() != null && !caregiver.getCertifications().isEmpty()) {
                stmt.setString(10, String.join(",", caregiver.getCertifications()));
            } else {
                stmt.setNull(10, Types.VARCHAR);
            }
            if (caregiver.getEmploymentType() != null) {
                stmt.setString(11, caregiver.getEmploymentType().name());
            } else {
                stmt.setNull(11, Types.VARCHAR);
            }

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
                return mapResultSetToCaregiver(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Caregiver getCaregiverByAppointmentID(int appointmentID) {
        String sql = "{CALL GetCaregiverByAppointmentID(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, appointmentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCaregiver(rs);
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
            if (caregiver.getDateOfBirth() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(caregiver.getDateOfBirth()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            if (caregiver.getGender() != null) {
                stmt.setString(7, caregiver.getGender().name());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }
            stmt.setString(8, caregiver.getContactNumber());
            stmt.setString(9, caregiver.getEmail());
            stmt.setString(10, caregiver.getAddress());
            if (caregiver.getCertifications() != null && !caregiver.getCertifications().isEmpty()) {
                stmt.setString(11, String.join(",", caregiver.getCertifications()));
            } else {
                stmt.setNull(11, Types.VARCHAR);
            }
            if (caregiver.getEmploymentType() != null) {
                stmt.setString(12, caregiver.getEmploymentType().name());
            } else {
                stmt.setNull(12, Types.VARCHAR);
            }
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
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCaregiverBackgroundStatus(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiverBackgroundStatus(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiver.getCaregiverID());
            if (caregiver.getBackgroundCheckStatus() != null) {
                stmt.setString(2, caregiver.getBackgroundCheckStatus().name());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCaregiverMedicalClearanceStatus(Caregiver caregiver) {
        String sql = "{CALL UpdateCaregiverMedicalClearanceStatus(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1, caregiver.getCaregiverID());
            if (caregiver.getMedicalClearanceStatus() != null) {
                stmt.setString(2, caregiver.getMedicalClearanceStatus().name());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.executeUpdate();
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
        String bgStatusString = rs.getString("backgroundCheckStatus");
        if (bgStatusString != null && !bgStatusString.trim().isEmpty()) {
            try {
                bgStatus = Caregiver.BackgroundCheckStatus.valueOf(bgStatusString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized background status value from DB: " + bgStatusString);
            }
        }

        Caregiver.MedicalClearanceStatus medStatus = null;
        String medStatusString = rs.getString("medicalClearanceStatus");
        if (medStatusString != null && !medStatusString.trim().isEmpty()) {
            try {
                medStatus = Caregiver.MedicalClearanceStatus.valueOf(medStatusString.trim());
            } catch (IllegalArgumentException e) {
                System.err.println("Warning: Unrecognized medical status value from DB: " + medStatusString);
            }
        }

        Caregiver.EmploymentType employmentType = null;
        String employmentTypeString = rs.getString("employmentType");
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
                rs.getString("firstName"),
                rs.getString("lastName"),
                rs.getTimestamp("dateOfBirth") != null ? rs.getTimestamp("dateOfBirth").toLocalDateTime() : null,
                gender,
                rs.getString("contactNumber"),
                rs.getString("email"),
                rs.getString("address"),
                certificationsList,
                bgStatus,
                medStatus,
                rs.getString("availabilitySchedule"),
                employmentType
        );


    }

    public Caregiver findByUsernameAndPassword(String username, String password) {
        String sql = "{CALL FindCaregiverByUsernameAndPassword(?,?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCaregiver(rs);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
