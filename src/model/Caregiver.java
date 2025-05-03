package model;

import java.time.LocalDateTime;
import java.util.List;

public class Caregiver {

    public enum Gender {
        MALE,
        FEMALE,
        OTHER
    }

    public enum BackgroundCheckStatus {
        PASSED,
        PENDING,
        FAILED,
    }

    public enum MedicalClearanceStatus {
        CLEARED,
        PENDING,
        NOT_CLEARED,
    }

    public enum EmploymentType {
        FULL_TIME,
        PART_TIME,
        RETIRED
    }
 
    private int caregiverID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private Gender gender;
    private String contactNumber;
    private String email;
    private String address;
    private List<String> certifications;
    private BackgroundCheckStatus backgroundCheckStatus;
    private MedicalClearanceStatus medicalClearanceStatus;
    private String availabilitySchedule;
    private EmploymentType employmentType;

    public Caregiver(int caregiverID, String username, String password, String firstName, String lastName, LocalDateTime dateOfBirth,
                     Gender gender, String contactNumber, String email, String address, List<String> certifications,
                     BackgroundCheckStatus backgroundCheckStatus, MedicalClearanceStatus medicalClearanceStatus, String availabilitySchedule, EmploymentType employmentType) {
        this.caregiverID = caregiverID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.certifications = certifications;
        this.backgroundCheckStatus = backgroundCheckStatus;
        this.medicalClearanceStatus = medicalClearanceStatus;
        this.availabilitySchedule = availabilitySchedule;
        this.employmentType = employmentType;
    }

    public Caregiver() {
        this.caregiverID = 0;
        this.username = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = null;
        this.gender = null;
        this.contactNumber = "";
        this.email = "";
        this.address = "";
        this.certifications = null;
        this.backgroundCheckStatus = null;
        this.medicalClearanceStatus = null;
        this.availabilitySchedule = "";
        this.employmentType = null;
    }

    public int getCaregiverID() {
        return caregiverID;
    }

    public void setCaregiverID(int caregiverID) {
        this.caregiverID = caregiverID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public BackgroundCheckStatus getBackgroundCheckStatus() {
        return backgroundCheckStatus;
    }

    public void setBackgroundCheckStatus(BackgroundCheckStatus backgroundCheckStatus) {
        this.backgroundCheckStatus = backgroundCheckStatus;
    }

    public MedicalClearanceStatus getMedicalClearanceStatus() {
        return medicalClearanceStatus;
    }

    public void setMedicalClearanceStatus(MedicalClearanceStatus medicalClearanceStatus) {
        this.medicalClearanceStatus = medicalClearanceStatus;
    }

    public String getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public void setAvailabilitySchedule(String availabilitySchedule) {
        this.availabilitySchedule = availabilitySchedule;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }
}