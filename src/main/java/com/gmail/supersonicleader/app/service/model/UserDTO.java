package com.gmail.supersonicleader.app.service.model;

import java.time.LocalDate;

import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;

public class UserDTO {

    private Integer id;
    private String username;
    private String password;
    private LocalDate createdBy;
    private UserRoleEnum userRole;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCreatedBy(LocalDate createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedBy() {
        return createdBy;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdBy=" + createdBy +
                ", userRole=" + userRole +
                '}';
    }

}
