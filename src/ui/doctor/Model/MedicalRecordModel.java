
package ui.doctor.Model;

public class MedicalRecordModel {
    private int id;
    private String patientName;
    private String disease;
    private String startDate;          // Kiểu String dd/MM/yyyy
    private String endDate;            // Kiểu String dd/MM/yyyy
    private int treatmentDurationDays; // Đưa số ngày xuống vị trí thứ 7
    private String currentStage;
    private String avatarPath;

    // Constructor mặc định
    public MedicalRecordModel() {}

    // Constructor đầy đủ tham số - Khớp chính xác với Controller
    public MedicalRecordModel(int id, String patientName, String disease, 
                              String startDate, String endDate, int treatmentDurationDays, 
                              String currentStage, String avatarPath) {
        this.id = id;
        this.patientName = patientName;
        this.disease = disease;
        this.startDate = startDate;
        this.endDate = endDate;
        this.treatmentDurationDays = treatmentDurationDays;
        this.currentStage = currentStage;
        this.avatarPath = avatarPath;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDisease() { return disease; }
    public void setDisease(String disease) { this.disease = disease; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getTreatmentDurationDays() { return treatmentDurationDays; }
    public void setTreatmentDurationDays(int treatmentDurationDays) { this.treatmentDurationDays = treatmentDurationDays; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
}