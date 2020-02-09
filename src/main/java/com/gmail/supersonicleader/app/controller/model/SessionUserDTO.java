package com.gmail.supersonicleader.app.controller.model;

import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;

public class SessionUserDTO {

    private String username;
    private UserRoleEnum userRole;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

}
