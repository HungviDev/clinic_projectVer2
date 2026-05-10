package model.user;

import java.sql.Timestamp;

public class Payment {

    private double amount;
    private String method;
    private String status;
    private Timestamp createdAt;
    private String serviceName;

    public Payment(double amount,
                   String method,
                   String status,
                   Timestamp createdAt,
                   String serviceName) {

        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.serviceName = serviceName;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getServiceName() {
        return serviceName;
    }
}