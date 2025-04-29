package controller;

import dao.AppointmentDAO;
import model.Appointment;

import java.sql.Connection;
import java.util.List;

public class AppointmentController {

    private final AppointmentDAO appointmentDAO;

    public AppointmentController(Connection conn) {
        this.appointmentDAO = new AppointmentDAO(conn);
    }

    public void addAppointment(Appointment appointment) {
        appointmentDAO.insertAppointment(appointment);
    }

    public Appointment getAppointmentById(int id) {
        return appointmentDAO.getAppointmentById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public void updateAppointment(Appointment appointment) {
        appointmentDAO.updateAppointment(appointment);
    }

    public void deleteAppointment(int id) {
        appointmentDAO.deleteAppointment(id);
    }
}