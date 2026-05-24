package model.admin;

import java.sql.Date;

public class AppointmentModel {
    private int id;

    private String doctorName;

    private String patientName;

    private Date appointmentDate;

    private String status;

    public AppointmentModel() {
    }

    public AppointmentModel(
            int id,
            String doctorName,
            String patientName,
            Date appointmentDate,
            String status
    ) {
        this.id = id;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
