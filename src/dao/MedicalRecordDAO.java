package dao;

import model.MedicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    private final Connection conn;

    public MedicalRecordDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertMedicalRecord(MedicalRecord record) {
        String sql =  "INSERT INTO medicalrecord (\n" +
                "    elder_id,\n" +
                "    diagnosis,\n" +
                "    medications,\n" +
                "    treatmentPlan,\n" +
                "    medicationStatus,\n" +
                "    treatmentStatus\n" +
                ") \n" +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, record.getElderID());
            stmt.setString(2, record.getDiagnosis());
            stmt.setString(3, record.getMedications());
            stmt.setString(4, record.getTreatmentPlan());
            stmt.setString(5, record.getMedicationStatus().name());;
            stmt.setString(6, record.getTreatmentStatus().name());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MedicalRecord getMedicalRecordById(int id) {
        String sql = "Select * FROM medicalrecord WHERE medicalRecord_id = ?";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedicalRecord(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "Select * FROM medicalrecord";
        try (CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                records.add(mapResultSetToMedicalRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public MedicalRecord getMedicalRecordByElderId(int elderID) {
        String sql = "SELECT * FROM medicalrecord WHERE elder_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, elderID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMedicalRecord(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateMedicalRecord(MedicalRecord record) {
        String sql = "{CALL UpdateMedicalRecord(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, record.getMedicalRecordID());
            stmt.setString(2, record.getDiagnosis());
            stmt.setString(3, record.getMedications());
            stmt.setString(4, record.getTreatmentPlan());
            stmt.setString(5, record.getMedicationStatus().name());
            stmt.setString(6, record.getTreatmentStatus().name());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMedicalRecord(int id) {
        String sql = "DELETE FROM medicalrecord WHERE medicalRecord_id = ?";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private MedicalRecord mapResultSetToMedicalRecord(ResultSet rs) throws SQLException {
        return new MedicalRecord(
                rs.getInt("medicalRecord_id"),
                rs.getString("diagnosis"),
                rs.getString("medications"),
                rs.getString("treatmentPlan"),
                MedicalRecord.Status.valueOf(rs.getString("medicationStatus")),
                MedicalRecord.Status.valueOf(rs.getString("treatmentStatus")),
                rs.getTimestamp("lastModified").toLocalDateTime(),
                rs.getInt("elder_id")
        );
    }
}