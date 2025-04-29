package model;

import java.time.LocalDateTime;
import java.util.List;

public class Caregiver {

    public enum Gender{
        MALE,
        FEMALE,
        OTHER
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
    private boolean backgroundCheckStatus;
    private boolean medicalClearanceStatus;
    private String availabilitySchedule;
    private String employmentType;

    public Caregiver(int caregiverID, String username, String password, String firstName, String lastName, LocalDateTime dateOfBirth,
                     Gender gender, String contactNumber, String email, String address, List<String> certifications,
                     boolean backgroundCheckStatus, boolean medicalClearanceStatus, String availabilitySchedule, String employmentType) {
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
        this.backgroundCheckStatus = false;
        this.medicalClearanceStatus = false;
        this.availabilitySchedule = "";
        this.employmentType = "";
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

    public boolean isBackgroundCheckStatus() {
        return backgroundCheckStatus;
    }

    public void setBackgroundCheckStatus(boolean backgroundCheckStatus) {
        this.backgroundCheckStatus = backgroundCheckStatus;
    }

    public boolean isMedicalClearanceStatus() {
        return medicalClearanceStatus;
    }

    public void setMedicalClearanceStatus(boolean medicalClearanceStatus) {
        this.medicalClearanceStatus = medicalClearanceStatus;
    }

    public String getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public void setAvailabilitySchedule(String availabilitySchedule) {
        this.availabilitySchedule = availabilitySchedule;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }
}