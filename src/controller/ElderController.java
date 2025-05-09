package controller;

import dao.ElderDAO;
import model.Elder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ElderController {

    private final ElderDAO elderDAO;

    public ElderController(Connection conn) {
        elderDAO = new ElderDAO(conn);
    }

    public void addElder(Elder elder) throws SQLException {
        elderDAO.insertElder(elder);
    }

    public Elder getElderById(int elderId) {
        return elderDAO.getElderById(elderId);
    }

    public List<Elder> getAllElders() {
        return elderDAO.getAllElders();
    }

    public void updateElder(Elder elder) {
        elderDAO.updateElder(elder);
    }

    public void deleteElder(int elderId) {
        elderDAO.deleteElder(elderId);
    }


    public List<Elder> getAllEldersByGuardianId(int guardianId) {
        return elderDAO.retrieveEldersWithGuardianId(guardianId);
    }

    public List<Elder> getAllEldersByAppointmentId(int appointmentID) {
        return elderDAO.getAllEldersByAppointmentId(appointmentID);
    }


    public String getRelationshipByGuardianId(int guardianID) {
        return elderDAO.getRelationshipByGuardianId(guardianID);
    }
}
