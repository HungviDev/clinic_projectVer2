package ui.doctor.Model;

public class AppointmentModel {
    private int id;

    private String time;
    private String patientName;
    private String serviceName;
    private String status;

    public AppointmentModel() {
    }

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
    public int getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}