package controller;

import dao.MedicalRecordDAO;
import model.Appointment;
import model.MedicalRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordController {

    private final MedicalRecordDAO medicalRecordDAO;

    public MedicalRecordController(Connection conn) {
        medicalRecordDAO = new MedicalRecordDAO(conn);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordDAO.insertMedicalRecord(medicalRecord);
    }

    public MedicalRecord getMedicalRecordById(int medicalRecordId) {
        return medicalRecordDAO.getMedicalRecordById(medicalRecordId);
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordDAO.getAllMedicalRecords();
    }

    public MedicalRecord getMedicalRecordByElderId(int elderId) {
        return medicalRecordDAO.getMedicalRecordById(elderId);
    }

    public void updateMedicalRecord(MedicalRecord medicalRecord) {
        medicalRecordDAO.updateMedicalRecord(medicalRecord);
    }

    public void deleteMedicalRecord(int medicalRecordId) {
        medicalRecordDAO.deleteMedicalRecord(medicalRecordId);
    }

}
