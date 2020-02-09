package com.gmail.supersonicleader.app.service;

import java.util.List;

import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;
import com.gmail.supersonicleader.app.service.exception.UserException;
import com.gmail.supersonicleader.app.service.model.AddUserDTO;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import com.gmail.supersonicleader.app.service.model.LoginUserDTO;

public interface UserService {

    void add(AddUserDTO addUserDTO) throws UserException;

    List<UserDTO> findAll() throws UserException;

    boolean verifyUser(LoginUserDTO userDTO) throws UserException;

    UserRoleEnum getUserRole(LoginUserDTO userDTO) throws UserException;

}
