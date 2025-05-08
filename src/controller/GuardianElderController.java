package controller;

import dao.GuardianElderDAO;
import model.Elder;
import model.GuardianElder;

import java.sql.Connection;
import java.util.List;

public class GuardianElderController {

    private final GuardianElderDAO guardianElderDAO;

    public GuardianElderController(Connection conn) {
        this.guardianElderDAO = new GuardianElderDAO(conn);
    }

    public void linkGuardianToElder(GuardianElder guardianElder) {
        guardianElderDAO.insertGuardianElderLink(guardianElder);
    }

    public List<Elder> getAllEldersByGuardianId(int guardianId) {
        return guardianElderDAO. getAllEldersByGuardianId(guardianId);
    }

    public GuardianElder getGuardianElderRelationshipByIds(int guardianId, int elderId) {
        return guardianElderDAO.getGuardianElderRelationshipByIds(guardianId, elderId);
    }

    public void updateGuardianElderRelationship(GuardianElder guardianElder) {
        guardianElderDAO.updateGuardianElderRelationship(guardianElder);
    }
}
