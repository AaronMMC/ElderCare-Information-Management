package controller;

import dao.AppointmentDAO;
import model.Appointment;

import java.sql.*;
import java.util.List;

public class AppointmentController {

    private final AppointmentDAO appointmentDAO;

    public AppointmentController(Connection conn) {
        this.appointmentDAO = new AppointmentDAO(conn);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public List<Appointment> getAllAppointmentsByCaregiver(int caregiverID) {
        return appointmentDAO.getAllAppointmentsByCaregiver(caregiverID);
    }

    public List<Appointment> getAllAppointmentsByGuardian(int guardianID) {
        return appointmentDAO.getAllAppointmentsByGuardian(guardianID);
    }

    public Appointment getAppointmentById(int id) {
        return appointmentDAO.getAppointmentById(id);
    }

    public int addAppointment(Appointment appointment) {
        return appointmentDAO.insertAppointment(appointment);
    }

    public void updateAppointment(Appointment appointment) {
        appointmentDAO.updateAppointment(appointment);
    }
}