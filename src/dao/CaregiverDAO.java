package dao;

import model.Caregiver;
import model.Service;

import java.sql.Connection;
import java.util.List;

public class CaregiverDAO {

    private final Connection conn;

    public CaregiverDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertCaregiver(Caregiver caregiver){

    };
    public Caregiver getCaregiverById(int id){

        return null;
    };
    public void updateCaregiver(Caregiver caregiver){

    };
    public void deleteCaregiver(int id){

    };
    public void insertCaregiverService(int caregiverId, int serviceId, int years, double rate){

    };
    public List<Service> getServicesByCaregiver(int caregiverId){
        return null;
    };

}
