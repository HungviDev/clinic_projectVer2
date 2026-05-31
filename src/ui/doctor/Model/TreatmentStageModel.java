package ui.doctor.Model;

public class TreatmentStageModel {
    private int id;
    private int treatmentRouteId;
    private String stageName;
    private int sequenceOrder;
    private String status; 
    private String note;

    public TreatmentStageModel(int id, int treatmentRouteId, String stageName, int sequenceOrder, String status, String note) {
        this.id = id;
        this.treatmentRouteId = treatmentRouteId;
        this.stageName = stageName;
        this.sequenceOrder = sequenceOrder;
        this.status = status;
        this.note = note;
    }

    // Getters và Setters
    public int getId() { return id; }
    public int getTreatmentRouteId() { return treatmentRouteId; }
    public String getStageName() { return stageName; }
    public int getSequenceOrder() { return sequenceOrder; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; }
}