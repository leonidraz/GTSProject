package com.project_gts.project_gts.models;

import java.sql.Date;

public class Receipt {
    private int id;
    private int subscriberId;
    private Date period;
    private double subscriptionFee;
    private double intercityFee;
    private double penalty;
    private double totalToPay;
    private Date paymentDeadline;
    private String status;

    public Receipt(int id, int subscriberId, Date period, double subscriptionFee, double intercityFee, double penalty, double totalToPay, Date paymentDeadline, String status) {
        this.id = id;
        this.subscriberId = subscriberId;
        this.period = period;
        this.subscriptionFee = subscriptionFee;
        this.intercityFee = intercityFee;
        this.penalty = penalty;
        this.totalToPay = totalToPay;
        this.paymentDeadline = paymentDeadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public double getSubscriptionFee() {
        return subscriptionFee;
    }

    public void setSubscriptionFee(double subscriptionFee) {
        this.subscriptionFee = subscriptionFee;
    }

    public double getIntercityFee() {
        return intercityFee;
    }

    public void setIntercityFee(double intercityFee) {
        this.intercityFee = intercityFee;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(double totalToPay) {
        this.totalToPay = totalToPay;
    }

    public Date getPaymentDeadline() {
        return paymentDeadline;
    }

    public void setPaymentDeadline(Date paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
