package model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private double totalAmount;
    private String paymentMethod;
    private double additionalCharges;
    private String currency;
    private LocalDateTime transactionDate;

    public Payment(int paymentID, double totalAmount, String paymentMethod, double additionalCharges, String currency, LocalDateTime transactionDate) {
        this.paymentID = paymentID;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.additionalCharges = additionalCharges;
        this.currency = currency;
        this.transactionDate = transactionDate;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(double additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
