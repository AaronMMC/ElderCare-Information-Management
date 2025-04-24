package util;

import java.time.LocalDateTime;

public class MedicalRecord {

    public enum Status {
        ONGOING,
        COMPLETED,
        PENDING,
        NOT_APPLICABLE
    }

    private int medicalRecordID;
    private String diagnosis;
    private String medications;
    private String treatmentPlan;
    private Status medicationStatus;
    private Status treatmentStatus;
    private LocalDateTime lastModified;

    public MedicalRecord(int medicalRecordID, String diagnosis, String medications, String treatmentPlan, Status medicationStatus, Status treatmentStatus, LocalDateTime lastModified) {
        this.medicalRecordID = medicalRecordID;
        this.diagnosis = diagnosis;
        this.medications = medications;
        this.treatmentPlan = treatmentPlan;
        this.medicationStatus = medicationStatus;
        this.treatmentStatus = treatmentStatus;
        this.lastModified = lastModified;
    }

    public int getMedicalRecordID() {
        return medicalRecordID;
    }

    public void setMedicalRecordID(int medicalRecordID) {
        this.medicalRecordID = medicalRecordID;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public Status getMedicationStatus() {
        return medicationStatus;
    }

    public void setMedicationStatus(Status medicationStatus) {
        this.medicationStatus = medicationStatus;
    }

    public Status getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(Status treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
