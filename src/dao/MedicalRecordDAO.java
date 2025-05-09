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
                "    diagnosis,\n" +
                "    medications,\n" +
                "    treatmentPlan,\n" +
                "    medicationStatus,\n" +
                "    treatmentStatus\n" +
                ") \n" +
                "VALUES (?, ?, ?, ?, ?);";
        try (CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, record.getDiagnosis());
            stmt.setString(2, record.getMedications());
            stmt.setString(3, record.getTreatmentPlan());
            stmt.setString(4, record.getMedicationStatus().name());;
            stmt.setString(5, record.getTreatmentStatus().name());
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
        String sql = "SELECT mr.*\n" +
                "    FROM medicalrecord mr\n" +
                "    INNER JOIN guardianelder ge ON mr.elder_id = ge.elder_id\n" +
                "    INNER JOIN guardian g ON ge.guardian_id = g.guardian_id\n" +
                "    WHERE mr.elder_id = ?";
        try (CallableStatement stmt = conn.prepareCall(sql)){
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
                rs.getInt("medical_record_id"),
                rs.getString("diagnosis"),
                rs.getString("medications"),
                rs.getString("treatment_plan"),
                MedicalRecord.Status.valueOf(rs.getString("medication_status")),
                MedicalRecord.Status.valueOf(rs.getString("treatment_status")),
                rs.getTimestamp("last_modified").toLocalDateTime()
        );
    }
}