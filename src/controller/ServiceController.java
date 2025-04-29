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

    public void addService(Service service) {
        serviceDAO.insertService(service);
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
}
