package dao;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private final Connection conn;

    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertPayment(Payment payment) {
        System.out.println("The appointment id is : " + payment.getAppointmentID());
        try {
            CallableStatement stmt = conn.prepareCall("{call insert_payment(?, ?, ?, ?)}");
            stmt.setDouble(1, payment.getAppointmentID());
            stmt.setString(2, payment.getPaymentStatus().toString());
            stmt.setDouble(3, payment.getTotalAmount());
            stmt.setString(4, payment.getPaymentMethod().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Payment getPaymentByID(int paymentID)  {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, paymentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment getPaymentByAppointmentId(int appointmentID)  {
        String sql = "SELECT * FROM payment WHERE appointment_id = ?";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            stmt.setInt(1,appointmentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Payment> getAllPayments() {
        String sql = "Select * FROM payment";
        List<Payment> payments = new ArrayList<>();
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public void updatePayment(Payment payment) {
        try {
            CallableStatement stmt = conn.prepareCall("{call update_payment(?, ?, ?, ?)}");
            stmt.setInt(1, payment.getPaymentID());
            stmt.setString(2, payment.getPaymentStatus().toString());
            stmt.setDouble(3, payment.getTotalAmount());
            stmt.setString(4, payment.getPaymentMethod().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePayment(int paymentID) {
        String sql = "DELETE FROM payment \n" +
                "    WHERE payment_id = ?";
        try {
            CallableStatement stmt = conn.prepareCall(sql);
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment payment = new Payment(
                rs.getInt("payment_id"),
                rs.getInt("appointment_id"),
                Payment.PaymentStatus.valueOf(rs.getString("paymentStatus")),
                rs.getDouble("totalAmount"),
                Payment.PaymentMethod.valueOf(rs.getString("paymentMethod")),
                rs.getTimestamp("transactionDate").toLocalDateTime());
        System.out.print("it's id is " + payment.getPaymentID());

        return payment;

    }
}