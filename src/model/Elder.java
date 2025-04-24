package model;
//
//import java.time.LocalDateTime;
//
//public class Elder {
//    private int elderID;
//    private String firstName;
//    private String lastName;
//    private LocalDateTime dateOfBirth;
//    private String contactNumber;
//    private String email;
//    private String address;
//
//    public Elder(int elderID, String firstName, String lastName, LocalDateTime dateOfBirth, String contactNumber, String email, String address) {
//        this.elderID = elderID;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dateOfBirth = dateOfBirth;
//        this.contactNumber = contactNumber;
//        this.email = email;
//        this.address = address;
//    }
//
//    public int getElderID() {
//        return elderID;
//    }
//
//    public void setElderID(int elderID) {
//        this.elderID = elderID;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public LocalDateTime getDateOfBirth() {
//        return dateOfBirth;
//    }
//
//    public void setDateOfBirth(LocalDateTime dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }
//
//    public String getContactNumber() {
//        return contactNumber;
//    }
//
//    public void setContactNumber(String contactNumber) {
//        this.contactNumber = contactNumber;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//}
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Elder {
    private final StringProperty name;
    private final StringProperty age;
    private final StringProperty sex;
    private final StringProperty email;
    private final StringProperty contact;
    private final StringProperty healthCondition;

    public Elder(String name, String age, String sex, String email, String contact, String healthCondition) {
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleStringProperty(age);
        this.sex = new SimpleStringProperty(sex);
        this.email = new SimpleStringProperty(email);
        this.contact = new SimpleStringProperty(contact);
        this.healthCondition = new SimpleStringProperty(healthCondition);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty ageProperty() { return age; }
    public StringProperty sexProperty() { return sex; }
    public StringProperty emailProperty() { return email; }
    public StringProperty contactProperty() { return contact; }
    public StringProperty healthConditionProperty() { return healthCondition; }

    // Optional: getters
    public String getName() { return name.get(); }
    public String getAge() { return age.get(); }
    public String getSex() { return sex.get(); }
    public String getEmail() { return email.get(); }
    public String getContact() { return contact.get(); }
    public String getHealthCondition() { return healthCondition.get(); }

    // Optional: setters
    public void setName(String name) { this.name.set(name); }
    public void setAge(String age) { this.age.set(age); }
    public void setSex(String sex) { this.sex.set(sex); }
    public void setEmail(String email) { this.email.set(email); }
    public void setContact(String contact) { this.contact.set(contact); }
    public void setHealthCondition(String healthCondition) { this.healthCondition.set(healthCondition); }
}
