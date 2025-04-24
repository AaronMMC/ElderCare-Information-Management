//package controller;
//
//import dao.CaregiverDAO;
//import model.Caregiver;
//import model.Service;
//
//import java.util.List;
//
//public class CaregiverController {
//
//    private final CaregiverDAO caregiverDAO;
//
//    public CaregiverController() {
//        this.caregiverDAO = new CaregiverDAO(); // or inject it if you're using a framework
//    }
//
//    public void addCaregiver(Caregiver caregiver) {
//        caregiverDAO.insertCaregiver(caregiver);
//    }
//
//    public Caregiver getCaregiverById(int caregiverId) {
//        return caregiverDAO.getCaregiverById(caregiverId);
//    }
//
//    public void updateCaregiver(Caregiver caregiver) {
//        caregiverDAO.updateCaregiver(caregiver);
//    }
//
//    public void deleteCaregiver(int caregiverId) {
//        caregiverDAO.deleteCaregiver(caregiverId);
//    }
//
//    public void assignServiceToCaregiver(int caregiverId, int serviceId, int experienceYears, double hourlyRate) {
//        caregiverDAO.insertCaregiverService(caregiverId, serviceId, experienceYears, hourlyRate);
//    }
//
//    public List<Service> getCaregiverServices(int caregiverId) {
//        return caregiverDAO.getServicesByCaregiver(caregiverId);
//    }
//}