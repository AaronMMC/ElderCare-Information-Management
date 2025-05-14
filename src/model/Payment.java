package model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private int appointmentID;
    private double amountPaid;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate; // this will only be set when the payment is completed

    public Payment(int paymentID, int appointmentID, double amountPaid, PaymentMethod paymentMethod, LocalDateTime transactionDate) {
        this.paymentID = paymentID;
        this.appointmentID = appointmentID;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionDate = transactionDate;
    }

    public Payment() {}


    public Payment(int appointmentID, double amountPaid, PaymentMethod paymentMethod) {
        this.appointmentID = appointmentID;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
    }

    public Payment(int appointmentID, double amountPaid, PaymentMethod paymentMethod, LocalDateTime transactionDate) {
        this.appointmentID = appointmentID;
        this.amountPaid = amountPaid;
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

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
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
}
