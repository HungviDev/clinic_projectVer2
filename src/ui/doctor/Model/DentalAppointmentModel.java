package ui.doctor.Model;

public class DentalAppointmentModel {

    private int id;
    private String patientName;
    private String appointmentDate;
    private String appointmentTime;
    private String problem;
    private String status;
    
    // THÊM BIẾN MỚI
    private String stageName;

    public DentalAppointmentModel() {
    }

    public DentalAppointmentModel(
            int id,
            String patientName,
            String appointmentDate,
            String appointmentTime,
            String problem,
            String status,
            String stageName // Cập nhật constructor
    ) {
        this.id = id;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.problem = problem;
        this.status = status;
        this.stageName = stageName; // Gán giá trị
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ==========================================
    // GETTER & SETTER CHO STAGE_NAME
    // ==========================================
    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }
}