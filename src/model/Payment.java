package model;

import java.time.LocalDateTime;

public class Payment {

    private int paymentID;
    private int appointmentID;
    private PaymentStatus paymentStatus;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate; // this will only be set when the payment is completed

    public Payment(int paymentID, int appointmentID, PaymentStatus paymentStatus, double totalAmount, PaymentMethod paymentMethod, LocalDateTime transactionDate) {
        this.paymentID = paymentID;
        this.appointmentID = appointmentID;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
    }


    public Payment(int appointmentID, PaymentStatus paymentStatus, double totalAmount, PaymentMethod paymentMethod) {
        this.appointmentID = appointmentID;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    public Payment(int appointmentID, PaymentStatus paymentStatus, double totalAmount, PaymentMethod paymentMethod, LocalDateTime transactionDate) {
        this.appointmentID = appointmentID;
        this.paymentStatus = paymentStatus;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
    }


    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public enum PaymentMethod {
        CASH,
        E_WALLET,
        CREDIT,
        DEBIT,
        OTHER
    }


    public enum PaymentStatus {
        PENDING,
        PAID
    }
}
