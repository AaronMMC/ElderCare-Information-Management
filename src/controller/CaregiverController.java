package controller;

import dao.CaregiverDAO;
import model.Caregiver;

import java.sql.Connection;

public class CaregiverController {

    private final CaregiverDAO caregiverDAO;

    public CaregiverController(Connection conn) {
        this.caregiverDAO = new CaregiverDAO(conn);
    }

    public void addCaregiver(Caregiver caregiver) {
        caregiverDAO.insertCaregiver(caregiver);
    }

    public Caregiver getCaregiverById(int caregiverId) {
        return caregiverDAO.getCaregiverById(caregiverId);
    }

    public void updateCaregiver(Caregiver caregiver) {
        caregiverDAO.updateCaregiver(caregiver);
    }

    public void deleteCaregiver(int caregiverId) {
        caregiverDAO.deleteCaregiver(caregiverId);
    }
}