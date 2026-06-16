package model.admin;

public class PaymentSummaryModel {
    private int userId;
    private String patientName;
    private double totalAmount;
    private double paidAmount;
    private double unpaidAmount;

    public PaymentSummaryModel() {
    }

    public PaymentSummaryModel(int userId, String patientName, double totalAmount, double paidAmount, double unpaidAmount) {
        this.userId = userId;
        this.patientName = patientName;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.unpaidAmount = unpaidAmount;
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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(double unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }
}
