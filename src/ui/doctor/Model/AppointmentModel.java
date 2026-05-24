package ui.doctor.Model;

public class AppointmentModel {

    private int id;
    private String time;
    private String patientName;
    private String serviceName;
    private String status;

    // ==========================================
    // Constructor mặc định
    // ==========================================
    public AppointmentModel() {
    }

    // ==========================================
    // Constructor đầy đủ tham số
    // ==========================================
    public AppointmentModel(
            int id,
            String time,
            String patientName,
            String serviceName,
            String status
    ) {
        this.id = id;
        this.time = time;
        this.patientName = patientName;
        this.serviceName = serviceName;
        this.status = status;
    }

    // ==========================================
    // Getter / Setter cho ID
    // ==========================================
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ==========================================
    // Getter / Setter cho Time
    // ==========================================
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // ==========================================
    // Getter / Setter cho Patient Name
    // ==========================================
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    // ==========================================
    // Getter / Setter cho Service Name
    // ==========================================
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    // ==========================================
    // Getter / Setter cho Status
    // ==========================================
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ==========================================
    // Hiển thị object khi debug
    // ==========================================
    @Override
    public String toString() {
        return "AppointmentModel{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", patientName='" + patientName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}