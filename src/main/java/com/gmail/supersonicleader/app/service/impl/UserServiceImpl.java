package com.gmail.supersonicleader.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.gmail.supersonicleader.app.repository.ConnectionRepository;
import com.gmail.supersonicleader.app.repository.RoleRepository;
import com.gmail.supersonicleader.app.repository.UserRepository;
import com.gmail.supersonicleader.app.repository.impl.ConnectionRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.RoleRepositoryImpl;
import com.gmail.supersonicleader.app.repository.impl.UserRepositoryImpl;
import com.gmail.supersonicleader.app.repository.model.Role;
import com.gmail.supersonicleader.app.repository.model.User;
import com.gmail.supersonicleader.app.service.UserService;
import com.gmail.supersonicleader.app.service.enums.UserRoleEnum;
import com.gmail.supersonicleader.app.service.exception.UserException;
import com.gmail.supersonicleader.app.service.model.AddUserDTO;
import com.gmail.supersonicleader.app.service.model.LoginUserDTO;
import com.gmail.supersonicleader.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserService instance;
    private ConnectionRepository connectionRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private UserServiceImpl(
            ConnectionRepository connectionRepository,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserRepositoryImpl.getInstance(),
                    RoleRepositoryImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void add(AddUserDTO addUserDTO) throws UserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = convertDTOToDatabaseUser(connection, addUserDTO);
                userRepository.add(connection, user);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserException(e.getMessage());
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> findAll() throws UserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> users = userRepository.findAll(connection);
                List<UserDTO> usersDTO = convertDatabaseUserToDTO(users);
                connection.commit();
                return usersDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public boolean verifyUser(LoginUserDTO userDTO) throws UserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String username = userDTO.getUsername();
                User user = userRepository.getByName(connection, username);
                if (user != null) {
                    return verifyPassword(userDTO, user);
                }
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public UserRoleEnum getUserRole(LoginUserDTO userDTO) throws UserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                String username = userDTO.getUsername();
                User user = userRepository.getByName(connection, username);
                Role role = user.getRole();
                String roleName = role.getName();
                UserRoleEnum userRoleEnum = UserRoleEnum.valueOf(roleName.toUpperCase());
                return userRoleEnum;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserException(e.getMessage());
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private boolean verifyPassword(LoginUserDTO userDTO, User user) {
        String legalPassword = user.getPassword();
        String enteredPassword = userDTO.getPassword();
        return enteredPassword.equals(legalPassword);
    }

    private List<UserDTO> convertDatabaseUserToDTO(List<User> users) {
        return users.stream().
                map(this::convertDatabaseUserToDTO).
                collect(Collectors.toList());
    }

    private UserDTO convertDatabaseUserToDTO(User user) {
        Integer id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        LocalDate createdBy = user.getCreatedBy();

        Role role = user.getRole();
        String roleName = role.getName();
        UserRoleEnum userRole = UserRoleEnum.valueOf(roleName.toUpperCase());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setCreatedBy(createdBy);
        userDTO.setUserRole(userRole);
        return userDTO;
    }

    private User convertDTOToDatabaseUser(Connection connection, AddUserDTO addUserDTO) throws SQLException {
        String username = addUserDTO.getUsername();
        String password = addUserDTO.getPassword();
        LocalDate createdBy = LocalDate.now();

        UserRoleEnum userRole = addUserDTO.getUserRole();
        String roleName = userRole.toString().toLowerCase();
        Role role = roleRepository.getByName(connection, roleName);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedBy(createdBy);
        user.setRole(role);
        return user;
    }

}
