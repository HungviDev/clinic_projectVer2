package model.user;

import java.sql.Timestamp;

public class Booking {

    private int userId;
    private int doctorId;
    private int serviceId;
    private Timestamp appointmentDate;
    private String note;

    public Booking(
            int userId,
            int doctorId,
            int serviceId,
            Timestamp appointmentDate,
            String note
    ) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.note = note;
    }

    public int getUserId() {
        return userId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public String getNote() {
        return note;
    }
}