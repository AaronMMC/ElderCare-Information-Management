package controller;

import dao.PaymentDAO;
import model.Appointment;
import model.CaregiverService;
import model.Payment;
import model.Service;

import java.sql.Connection;
import java.util.List;

public class PaymentController {

    private final PaymentDAO paymentDAO;

    public PaymentController(Connection conn) {
        paymentDAO = new PaymentDAO(conn);
    }

    public void addPayment(Payment payment) {
        paymentDAO.insertPayment(payment);
    }

    public Payment getPaymentById(int paymentID) {
        return paymentDAO.getPaymentByID(paymentID);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
    }

    public void updatePayment(Payment payment) {
        paymentDAO.updatePayment(payment);
    }

    public void deletePayment(int paymentID) {
        paymentDAO.deletePayment(paymentID);
    }

    public Payment getPaymentByAppointmentId(int appointmentID) {
        return paymentDAO.getPaymentByAppointmentId(appointmentID);
    }

    public List<Payment> getPaymentsByAppointmentId(int appointmentID) {
        return paymentDAO.getPaymentsByAppointmentId(appointmentID);
    }
}
