package model.user;

import java.sql.Timestamp;

public class TreatmentHistory {

    private Timestamp createdAt;
    private String doctorName;
    private String diagnosis;
    private String treatmentPlan;
    private String statusStage;
    private int treatmentRouteId;

    public TreatmentHistory() {
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public String getstatusStage() {
        return statusStage;
    }

    public void setstatusStage(String statusStage) {
        this.statusStage = statusStage;
    }

    public int getTreatmentRouteId() {
        return treatmentRouteId;
    }

    public void setTreatmentRouteId(int treatmentRouteId) {
        this.treatmentRouteId = treatmentRouteId;
    }
}