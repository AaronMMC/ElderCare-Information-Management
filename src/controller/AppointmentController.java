package controller;

import model.Appointment;

import java.sql.Connection;
import java.util.List;

public class AppointmentController {

    private final MedicalRecordController.AppointmentController appointmentController;

    public AppointmentController(Connection conn) {
        this.appointmentController = new MedicalRecordController.AppointmentController(conn);
    }

    public void addAppointment(Appointment appointment) {
        appointmentController.insertAppointment(appointment);
    }

    public Appointment getAppointmentById(int id) {
        return appointmentController.getAppointmentById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentController.getAllAppointments();
    }

    public void updateAppointment(Appointment appointment) {
        appointmentController.updateAppointment(appointment);
    }

    public void deleteAppointment(int id) {
        appointmentController.deleteAppointment(id);
    }

    public List<Appointment> getAllAppointmentsByCaregiver(int caregiverID) {
        return appointmentController.getAllAppointmentsByCaregiver(caregiverID);
    }
}