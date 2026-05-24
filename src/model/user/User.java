 package model.user;

import java.sql.Date;
// import java.util.Date;

public class User {
    private int id;
    private String fullName;
    private String phone;
    private int roleId;
    private String password;

    private Date birthDate;

    private String address;

    private String avatar;

    private String email;

    public User() {}

    public User(int id, String fullName, String phone,String password, int roleId, Date birthDate, String address, String email, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.roleId = roleId;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.email = email;
        this.avatar = avatar;
    }

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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

