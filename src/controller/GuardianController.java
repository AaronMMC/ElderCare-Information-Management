package controller;

import dao.GuardianDAO;
import model.Guardian;

import java.sql.Connection;

public class GuardianController {

    private final GuardianDAO guardianDAO;

    public GuardianController(Connection conn) {
        this.guardianDAO = new GuardianDAO(conn);
    }

    public void addGuardian(Guardian guardian) {
        guardianDAO.insertGuardian(guardian);
    }

    public Guardian getGuardianById(int guardianId) {
        return guardianDAO.getGuardianById(guardianId);
    }

    public void updateGuardian(Guardian guardian) {
        guardianDAO.updateGuardian(guardian);
    }

    public void deleteGuardian(int guardianId) {
        guardianDAO.deleteGuardian(guardianId);
    }
}