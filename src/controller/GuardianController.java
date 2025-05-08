package controller;

import dao.AdminDAO;
import dao.CaregiverDAO;
import dao.GuardianDAO;
import model.Guardian;

import java.sql.Connection;

public class GuardianController {

    private final GuardianDAO guardianDAO;
    private final CaregiverDAO caregiverDAO;
    private final AdminDAO adminDAO;

    public GuardianController(Connection conn) {
        this.guardianDAO = new GuardianDAO(conn);
        this.caregiverDAO = new CaregiverDAO(conn);
        this.adminDAO = new AdminDAO(conn);
    }

    public void addGuardian(Guardian guardian) {
        if (isUsernameTaken(guardian.getUsername())) {
            throw new RuntimeException("Username '" + guardian.getUsername() + "' is already taken.");
        }
        guardianDAO.insertGuardian(guardian);
    }

    public Guardian getGuardianById(int guardianId) {
        return guardianDAO.getGuardianById(guardianId);
    }

    public Guardian findByUsername(String username){
        return guardianDAO.findByUsername(username);
    }

    public void updateGuardian(Guardian guardian) {
        // Consider adding username check here if username can be updated
        guardianDAO.updateGuardian(guardian);
    }

    public void deleteGuardian(int guardianId) {
        guardianDAO.deleteGuardian(guardianId);
    }

    public boolean isUsernameTaken(String username) {
        // Check if username exists in Guardian, Caregiver, or Admin tables
        boolean guardianExists = guardianDAO.findByUsername(username) != null;
        boolean caregiverExists = caregiverDAO.findByUsername(username) != null;
        boolean adminExists = adminDAO.findByUsername(username) != null; // Assuming AdminDAO has findByUsername

        return guardianExists || caregiverExists || adminExists;
    }

    public Guardian getGuardianByAppointmentId(int appointmentID) {
        return guardianDAO.getGuardianByAppointmentId(appointmentID);
    }
}
