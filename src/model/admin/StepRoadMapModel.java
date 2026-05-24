package model.admin;

import java.sql.Date;

public class StepRoadMapModel {

    private int id;

    private int treatmentRouteId;

    private String stageName;

    private int sequenceOrder;

    private String status;

    private Date appointmentDate;

    private String note;

    private Date createdAt;

    private Date updatedAt;

    private double cost;

    private int delay;

    public StepRoadMapModel() {
    }

    public StepRoadMapModel(
            int id,
            int treatmentRouteId,
            String stageName,
            int sequenceOrder,
            String status,
            Date appointmentDate,
            String note,
            Date createdAt,
            Date updatedAt,
            double cost,
            int delay
    ) {

        this.id = id;
        this.treatmentRouteId = treatmentRouteId;
        this.stageName = stageName;
        this.sequenceOrder = sequenceOrder;
        this.status = status;
        this.appointmentDate = appointmentDate;
        this.note = note;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cost = cost;
        this.delay = delay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTreatmentRouteId() {
        return treatmentRouteId;
    }

    public void setTreatmentRouteId(int treatmentRouteId) {
        this.treatmentRouteId = treatmentRouteId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "StepRoadMapModel{" +
                "id=" + id +
                ", treatmentRouteId=" + treatmentRouteId +
                ", stageName='" + stageName + '\'' +
                ", sequenceOrder=" + sequenceOrder +
                ", status='" + status + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", note='" + note + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", cost=" + cost +
                ", delay=" + delay +
                '}';
    }
}