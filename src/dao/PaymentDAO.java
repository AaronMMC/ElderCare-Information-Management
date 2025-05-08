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
        try {
            CallableStatement stmt = conn.prepareCall("{call insert_payment(?, ?, ?)}");
            stmt.setDouble(1, payment.getTotalAmount());
            stmt.setString(2, payment.getPaymentMethod().name());
            stmt.setDouble(3, payment.getAdditionalCharges());
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
                mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment getPaymentByAppointmentId(int appointmentID)  {
        String sql = "{CALL get_payment_by_appointment_id(?)}";
        try (CallableStatement stmt = conn.prepareCall(sql)){
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
               mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment getPaymentByAllServices(Appointment appointment, List<CaregiverService> caregiverServices, List<Service> services) {
        double totalServiceCost = 0.0;
        double additionalCharges = 0.0;
        int durationInSeconds = appointment.getDuration();
        double durationInHours = durationInSeconds / 3600.0; // Convert to hours

        // Sum up the prices of all selected services and calculate corresponding additional charges
        for (Service service : services) {
            totalServiceCost += service.getPrice();

            // Find the corresponding CaregiverService for the current Service
            CaregiverService correspondingCaregiverService = findCaregiverServiceForService(service, caregiverServices);

            if (correspondingCaregiverService != null) {
                double hourlyRate = correspondingCaregiverService.getHourlyRate();
                additionalCharges += hourlyRate * durationInHours;
            } else {
                // This case should ideally not happen based on your clarification,
                // but it's good practice to handle potential unexpected scenarios.
                System.err.println("Error: No CaregiverService found for service: " + service.getServiceName());
                // You might want to throw an exception or handle this differently.
            }
        }

        // Create new Payment object and set values
        Payment payment = new Payment();
        payment.setTotalAmount(totalServiceCost);
        payment.setAdditionalCharges(additionalCharges);

        return payment;
    }

    // You'll still need this method to link a Service to its CaregiverService
    private CaregiverService findCaregiverServiceForService(Service service, List<CaregiverService> caregiverServices) {
        for (CaregiverService caregiverService : caregiverServices) {
            if (caregiverService.getServiceId() == service.getServiceID()) {
                return caregiverService;
            }
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            CallableStatement stmt = conn.prepareCall("{call get_all_payments()}");
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
            CallableStatement stmt = conn.prepareCall("{call update_payment(?, ?, ?, ?, ?)}");
            stmt.setInt(1, payment.getPaymentID());
            stmt.setDouble(2, payment.getTotalAmount());
            stmt.setString(3, payment.getPaymentMethod().name());
            stmt.setDouble(4, payment.getAdditionalCharges());
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

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getDouble("total_amount"),
                Payment.PaymentMethod.valueOf(rs.getString("payment_method")),
                rs.getDouble("additional_charges"),
                rs.getTimestamp("transaction_date").toLocalDateTime());
    }
}