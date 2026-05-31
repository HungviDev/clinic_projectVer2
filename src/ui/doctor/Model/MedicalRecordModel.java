package ui.doctor.Model;

public class MedicalRecordModel {
    private int id;
    private String patientName;
    private String disease;
    private String startDate;
    private int treatmentRouteId;
    private String routeName;    // Đã thêm để khớp với View
    private String currentStage;
    private String avatarPath;

    // Constructor đầy đủ
    public MedicalRecordModel(int id, String patientName, String disease, 
                              String startDate, int treatmentRouteId, 
                              String routeName, String avatarPath) {
        this.id = id;
        this.patientName = patientName;
        this.disease = disease;
        this.startDate = startDate;
        this.treatmentRouteId = treatmentRouteId;
        this.routeName = routeName;
        this.avatarPath = avatarPath;
        this.currentStage = "Chưa xác định"; // Giá trị mặc định
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

    public int getTreatmentRouteId() { return treatmentRouteId; }
    public void setTreatmentRouteId(int treatmentRouteId) { this.treatmentRouteId = treatmentRouteId; }

    // Phương thức này giải quyết lỗi "getRouteName() is undefined"
    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getCurrentStage() { return currentStage; }
    public void setCurrentStage(String currentStage) { this.currentStage = currentStage; }

    public String getAvatarPath() { return avatarPath; }
    public void setAvatarPath(String avatarPath) { this.avatarPath = avatarPath; }
}