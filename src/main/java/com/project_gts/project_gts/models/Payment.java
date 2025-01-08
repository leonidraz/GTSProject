package com.project_gts.project_gts.models;

import java.util.Date;

public class Payment {
    private int id;
    private int receiptId;
    private String type;
    private Date paymentDate;
    private double paymentAmount;

    public Payment(int id, int receiptId, String type, Date paymentDate, double paymentAmount) {
        this.id = id;
        this.receiptId = receiptId;
        this.type = type;
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getType() {
        return type;
    }

    public void setPaymentType(String type) {
        this.type = type;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}

