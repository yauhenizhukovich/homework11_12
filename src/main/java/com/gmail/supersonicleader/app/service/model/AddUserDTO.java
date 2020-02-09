package com.gmail.supersonicleader.app.service.model;

import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;

public class AddUserDTO {

    private String username;
    private String password;
    private UserRoleEnum userRole;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

}