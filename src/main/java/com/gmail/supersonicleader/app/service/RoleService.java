package com.gmail.supersonicleader.app.service;

import java.util.List;

import com.gmail.supersonicleader.app.service.exception.RoleException;
import com.gmail.supersonicleader.app.service.model.AddRoleDTO;
import com.gmail.supersonicleader.app.service.model.RoleDTO;

public interface RoleService {

    void add(AddRoleDTO addRoleDTO) throws RoleException;

    List<RoleDTO> findAll() throws RoleException;

}
