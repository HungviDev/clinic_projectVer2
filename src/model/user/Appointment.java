package model.user;

import java.sql.Timestamp;

public class Appointment {

    private int id;
    private int userId;
    private int doctorId;
    private int serviceId;

    private String serviceName;
    private String status;
    private String note;
    private String doctorName;

    private Timestamp appointmentDate;

    public Appointment() {
    }

    public Appointment(int id,
                       Timestamp appointmentDate,
                       String status,
                       String note,
                       String serviceName,
                       String doctorName) {

        this.id = id;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.note = note;
        this.serviceName = serviceName;
        this.doctorName = doctorName;
    }

    // GETTER SETTER

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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}