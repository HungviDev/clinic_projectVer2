package model.admin;

import java.sql.Timestamp;

public class PaymentModel {

    private int id;
    private int userId;
    private String patientName;
    private double amount;
    private String method;
    private String status;
    private Timestamp createdAt;
    private int treatmentStageId;

    public PaymentModel() {
    }

    public PaymentModel(int id, int userId, String patientName, double amount, String method,
                        String status, Timestamp createdAt, int treatmentStageId) {
        this.id = id;
        this.userId = userId;
        this.patientName = patientName;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.createdAt = createdAt;
        this.treatmentStageId = treatmentStageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getTreatmentStageId() {
        return treatmentStageId;
    }

    public void setTreatmentStageId(int treatmentStageId) {
        this.treatmentStageId = treatmentStageId;
    }
}