package model.admin;

import java.sql.Date; // SỬA LỖI Ở ĐÂY: Đổi từ java.util.Date thành java.sql.Date

public class DoctorModel extends UserModel {

    private String specialization;

    private int experience;

    // =====================================
    // CONSTRUCTOR RỖNG
    // =====================================
    public DoctorModel() {

    }

    // =====================================
    // CONSTRUCTOR ĐẦY ĐỦ
    // ===================================
    public DoctorModel(
            int id,
            String fullName,
            String phone,
            String password,
            Date birthDate,    // Nhớ import java.sql.Date;
            String address,
            String avatar,
            String email,
            String specialization,
            int experience
    ) {

        // ==========================================
        // SỬA Ở ĐÂY: Truyền thêm số 2 (roleId của bác sĩ)
        // ==========================================
        super(
                id,
                fullName,
                phone,
                password,
                birthDate,
                address,
                avatar,
                email
        );

        this.specialization = specialization;
        this.experience = experience;
    }

    // =====================================
    // GETTER & SETTER
    // =====================================

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}