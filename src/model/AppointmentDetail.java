package model;

public class AppointmentDetail {

    private int id;

    private String patientName;

    private String phone;

    private String dob;

    private String address;

    private String service;

    private String doctor;

    private String date;

    private String time;

    private String status;

    public AppointmentDetail(
            int id,
            String patientName,
            String phone,
            String dob,
            String address,
            String service,
            String doctor,
            String date,
            String time,
            String status
    ) {

        this.id = id;
        this.patientName = patientName;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.service = service;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    public String getService() {
        return service;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}