package controller;

import dao.ServiceDAO;
import model.Service;

import java.sql.Connection;
import java.util.List;

public class ServiceController {

    private final ServiceDAO serviceDAO;

    public ServiceController(Connection conn) {
        serviceDAO = new ServiceDAO(conn);
    }

    public int addService(Service service) {
        return serviceDAO.insertService(service);
    }

    public Service getServiceById(int serviceID) {
        return serviceDAO.getServiceByID(serviceID);
    }

    public List<Service> getAllServices() {
        return serviceDAO.getAllServices();
    }

    public void updateService(Service service) {
        serviceDAO.updateService(service);
    }

    public void deleteService(int serviceID) {
        serviceDAO.deleteService(serviceID);
    }

    public List<Service> getAllServicesByAppointmentId(int appointmentID) {
        return serviceDAO.getAllServicesByAppointmentId(appointmentID);
    }

    public List<Service> getAllServicesByCaregiverId(int caregiverID) {
        return serviceDAO.getAllServicesByCaregiverId(caregiverID);
    }
}
