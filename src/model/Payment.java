package model;

import java.time.LocalDateTime;

public class Payment {

    public enum PaymentMethod {
        E_WALLET,
        CREDIT,
        DEBIT,
        OTHER
    }

    private int paymentID;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private double additionalCharges;
    private LocalDateTime transactionDate;

    public Payment(int paymentID, double totalAmount, PaymentMethod paymentMethod, double additionalCharges, LocalDateTime transactionDate) {
        this.paymentID = paymentID;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.additionalCharges = additionalCharges;
        this.transactionDate = transactionDate;
    }

    public Payment() {
        this.paymentID = 0;
        this.totalAmount = 0;
        this.paymentMethod = PaymentMethod.OTHER;
        this.additionalCharges = 0;
        this.transactionDate = null;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
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

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
