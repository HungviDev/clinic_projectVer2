package model.admin;

public class TreatmentDetail {

    private String patientId;

    private String patientName;

    private String diagnosis;

    private String routeName;

    private String doctorName;

    public TreatmentDetail() {
    }

    public TreatmentDetail(
            String patientId,
            String patientName,
            String diagnosis,
            String routeName,
            String doctorName
    ) {

        this.patientId = patientId;

        this.patientName = patientName;

        this.diagnosis = diagnosis;

        this.routeName = routeName;

        this.doctorName = doctorName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
}