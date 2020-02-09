package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.RoleRepository;
import com.gmail.supersonicleader.app.repository.impl.ConnectionRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.RoleRepositoryImpl;
import com.gmail.supersonicleader.app.repository.model.Role;
import com.gmail.supersonicleader.app.service.RoleService;
import com.gmail.supersonicleader.app.service.exception.RoleException;
import com.gmail.supersonicleader.app.service.model.AddRoleDTO;
import com.gmail.supersonicleader.app.service.model.RoleDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static RoleService instance;
    private ConnectionRepository connectionRepository;
    private RoleRepository roleRepository;

    private RoleServiceImpl(
            ConnectionRepository connectionRepository,
            RoleRepository roleRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.roleRepository = roleRepository;
    }

    public static RoleService getInstance() {
        if (instance == null) {
            instance = new RoleServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    RoleRepositoryImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void add(AddRoleDTO addRoleDTO) throws RoleException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Role role = convertDTOToDatabaseRole(addRoleDTO);
                roleRepository.add(connection, role);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RoleException(e.getMessage());
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<RoleDTO> findAll() throws RoleException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<Role> roles = roleRepository.findAll(connection);
                List<RoleDTO> rolesDTO = convertDatabaseRoleToDTO(roles);
                connection.commit();
                return rolesDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RoleException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private Role convertDTOToDatabaseRole(AddRoleDTO addRoleDTO) {
        Role role = new Role();
        String roleName = addRoleDTO.getName();
        String description = addRoleDTO.getDescription();
        role.setName(roleName);
        role.setDescription(description);
        return role;
    }

    private List<RoleDTO> convertDatabaseRoleToDTO(List<Role> roles) {
        return roles.stream().
                map(this::convertDatabaseRoleToDTO).
                collect(Collectors.toList());
    }

    private RoleDTO convertDatabaseRoleToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        Integer id = role.getId();
        String roleName = role.getName();
        String description = role.getDescription();
        roleDTO.setId(id);
        roleDTO.setName(roleName);
        roleDTO.setDescription(description);
        return roleDTO;
    }

}
