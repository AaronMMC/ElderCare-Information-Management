package controller;

import dao.PaymentDAO;
import model.Payment;

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

    public Payment getPaymentByAppointmentId(int paymentID) {
        return paymentDAO.getPaymentByAppointmentId(paymentID);
    }
}
