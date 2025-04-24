//package controller;
//
//import dao.GuardianDAO;
//import model.Elder;
//import model.Guardian;
//
//import java.util.List;
//
//public class GuardianController {
//
//    private final GuardianDAO guardianDAO;
//
//    public GuardianController() {
//        this.guardianDAO = new GuardianDAO();
//    }
//
//    public void addGuardian(Guardian guardian) {
//        guardianDAO.insertGuardian(guardian);
//    }
//
//    public Guardian getGuardianById(int guardianId) {
//        return guardianDAO.getGuardianById(guardianId);
//    }
//
//    public void updateGuardian(Guardian guardian) {
//        guardianDAO.updateGuardian(guardian);
//    }
//
//    public void deleteGuardian(int guardianId) {
//        guardianDAO.deleteGuardian(guardianId);
//    }
//
//    public void linkGuardianToElder(int guardianId, int elderId, String relationshipType) {
//        guardianDAO.insertGuardianElderLink(guardianId, elderId, relationshipType);
//    }
//
//    public List<Elder> getEldersByGuardian(int guardianId) {
//        return guardianDAO.getEldersByGuardianId(guardianId);
//    }
//}