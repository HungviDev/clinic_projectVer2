package ui.doctor.Model;

public class PatientModel {
    private String fullName;
    private String birthDate;
    private String phone;
    private String email;
    private String address;
    private String treatmentProblem;

    // Constructor rỗng (Rất cần thiết khi khởi tạo Model trước khi set data)
    public PatientModel() {
    }

    // Constructor đầy đủ
    public PatientModel(String fullName, String birthDate, String phone, String email, String address, String treatmentProblem) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.treatmentProblem = treatmentProblem;
    }

    // Getters
    public String getFullName() { return fullName != null ? fullName : ""; }
    public String getBirthDate() { return birthDate != null ? birthDate : "Chưa cập nhật"; }
    public String getPhone() { return phone != null ? phone : "Chưa cập nhật"; }
    public String getEmail() { return email != null ? email : "Chưa cập nhật"; }
    public String getAddress() { return address != null ? address : "Chưa cập nhật"; }
    public String getTreatmentProblem() { return treatmentProblem != null ? treatmentProblem : "Chưa xác định"; }

    // Setters (Để sếp linh hoạt trong việc gán giá trị sau này)
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setTreatmentProblem(String treatmentProblem) { this.treatmentProblem = treatmentProblem; }
}