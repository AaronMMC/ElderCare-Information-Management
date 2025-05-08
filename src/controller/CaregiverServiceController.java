package controller;

import dao.CaregiverServiceDAO;
import model.Caregiver;
import model.CaregiverService;
import model.Service;

import java.sql.Connection;
import java.util.List;

public class CaregiverServiceController {

    private final CaregiverServiceDAO caregiverServiceDAO;

    public CaregiverServiceController(Connection conn) {
        this.caregiverServiceDAO = new CaregiverServiceDAO(conn);
    }

    public void addCaregiverService(CaregiverService service) {
        caregiverServiceDAO.insertCaregiverService(service);
    }

    public CaregiverService getCaregiverService(int caregiverId, int serviceId) {
        return caregiverServiceDAO.getCaregiverService(caregiverId, serviceId);
    }

    public List<CaregiverService> getAllCaregiverServices() {
        return caregiverServiceDAO.getAllCaregiverServices();
    }

    public List<Service> getAllServicesByCaregiverId(int caregiverId) {
        return caregiverServiceDAO.getAllServicesByCaregiverId(caregiverId);
    }

    public Caregiver getCaregiverByServiceId(int serviceId) {
        return caregiverServiceDAO.getCaregiverByServiceId(serviceId);
    }

    public void updateCaregiverService(CaregiverService service) {
        caregiverServiceDAO.updateCaregiverService(service);
    }

    public void deleteCaregiverService(int caregiverId, int serviceId) {
        caregiverServiceDAO.deleteCaregiverService(caregiverId, serviceId);
    }
}