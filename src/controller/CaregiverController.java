package controller;

import dao.AdminDAO;
import dao.CaregiverDAO;
import dao.GuardianDAO;
import model.Caregiver;
import model.Service;

import java.sql.Connection;
import java.util.List;

public class CaregiverController {

    private final CaregiverDAO caregiverDAO;
    private final GuardianDAO guardianDAO;
    private final AdminDAO adminDAO;

    public CaregiverController(Connection conn) {
        this.caregiverDAO = new CaregiverDAO(conn);
        this.guardianDAO = new GuardianDAO(conn);
        this.adminDAO = new AdminDAO(conn);
    }

    public void addCaregiver(Caregiver caregiver) {
        if (isUsernameTaken(caregiver.getUsername())) {
            throw new RuntimeException("Username '" + caregiver.getUsername() + "' is already taken.");
        }
        caregiverDAO.insertCaregiver(caregiver);
    }

    public Caregiver getCaregiverById(int caregiverId) {
        return caregiverDAO.getCaregiverById(caregiverId);
    }

    public void updateCaregiver(Caregiver caregiver) {
        // Consider adding username check here if username can be updated
        caregiverDAO.updateCaregiver(caregiver);
    }

    public void deleteCaregiver(int caregiverId) {
        caregiverDAO.deleteCaregiver(caregiverId);
    }

    public boolean isUsernameTaken(String username) {
        // Check if username exists in Caregiver, Guardian, or Admin tables
        boolean caregiverExists = caregiverDAO.findByUsername(username) != null;
        boolean guardianExists = guardianDAO.findByUsername(username) != null; // Assuming GuardianDAO has findByUsername
        boolean adminExists = adminDAO.findByUsername(username) != null; // Assuming AdminDAO has findByUsername

        return caregiverExists || guardianExists || adminExists;
    }

    public List<Caregiver> getAllCaregivers() {
        return caregiverDAO.getAllCaregivers();
    }

    public List<Caregiver> getAllCaregiversByService(Service service) {
        return caregiverDAO.getAllCaregiversByServiceId(service.getServiceID());
    }
}
