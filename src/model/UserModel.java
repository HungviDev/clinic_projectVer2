package model;

import java.util.Date;

public class UserModel {

    private int id;

    private String fullName;

    private String phone;

    private String password;

    private Date birthDate;

    private String address;

    private String avatar;

    private String email;

    // =====================================
    // CONSTRUCTOR RỖNG
    // =====================================
    public UserModel() {

    }

    // =====================================
    // CONSTRUCTOR ĐẦY ĐỦ
    // =====================================
    public UserModel(
            int id,
            String fullName,
            String phone,
            String password,
            Date birthDate,
            String address,
            String avatar,
            String email
    ) {

        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.avatar = avatar;
        this.email = email;
    }

    // =====================================
    // GETTER & SETTER
    // =====================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}