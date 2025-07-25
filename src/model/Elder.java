package model;

import java.time.LocalDateTime;

public class Elder {
    private int elderID;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String contactNumber;
    private String email;
    private String address;
    private int guardianId;
    private String relationshipToGuardian;


    public Elder(int elderID, String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address, int guardianId, String relationshipToGuardian) {
        this.elderID = elderID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.guardianId = guardianId;
        this.relationshipToGuardian = relationshipToGuardian;
    }


    public Elder(String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address, int guardianId, String relationshipToGuardian) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.guardianId = guardianId;
        this.relationshipToGuardian = relationshipToGuardian;
    }


    public Elder(String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address, int guardianId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.guardianId = guardianId;
    }

    public Elder(int elderID, String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address) {
        this.elderID = elderID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    public Elder(String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    public Elder() {
        this.elderID = -1;
        this.firstName = "";
        this.lastName = "";
        this.dateOfBirth = null;
        this.contactNumber = "";
        this.email = "";
        this.address = "";
    }

    public int getElderID() {
        return elderID;
    }

    public void setElderID(int elderID) {
        this.elderID = elderID;
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

    public int getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
    }

    public String getRelationship() {
        return relationshipToGuardian;
    }

    public void setRelationship(String relationshipToGuardian) {
        this.relationshipToGuardian = relationshipToGuardian;
    }
}