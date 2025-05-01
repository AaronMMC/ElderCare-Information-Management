package dao;

import model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private final Connection conn;

    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertPayment(Payment payment) {
        try {
            CallableStatement stmt = conn.prepareCall("{call insert_payment(?, ?, ?, ?)}");
            stmt.setDouble(1, payment.getTotalAmount());
            stmt.setString(2, payment.getPaymentMethod());
            stmt.setDouble(3, payment.getAdditionalCharges());
            stmt.setString(4, payment.getCurrency());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Payment getPaymentByID(int paymentID)  {
        try {
            CallableStatement stmt = conn.prepareCall("{call get_payment_by_id(?)}");
            stmt.setInt(1, paymentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getDouble("additional_charges"),
                        rs.getString("currency"),
                        rs.getTimestamp("transaction_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            CallableStatement stmt = conn.prepareCall("{call get_all_payments()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method"),
                        rs.getDouble("additional_charges"),
                        rs.getString("currency"),
                        rs.getTimestamp("transaction_date").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    public void updatePayment(Payment payment) {
        try {
            CallableStatement stmt = conn.prepareCall("{call update_payment(?, ?, ?, ?, ?, ?)}");
            stmt.setInt(1, payment.getPaymentID());
            stmt.setDouble(2, payment.getTotalAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setDouble(4, payment.getAdditionalCharges());
            stmt.setString(5, payment.getCurrency());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePayment(int paymentID) {
        try {
            CallableStatement stmt = conn.prepareCall("{call delete_payment(?)}");
            stmt.setInt(1, paymentID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}