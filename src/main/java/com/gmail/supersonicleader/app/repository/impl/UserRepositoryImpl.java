package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.app.repository.UserRepository;
import com.gmail.supersonicleader.app.repository.model.Role;
import com.gmail.supersonicleader.app.repository.model.User;

public class UserRepositoryImpl extends GeneralRepositoryImpl<User> implements UserRepository {

    private static UserRepository instance;

    private UserRepositoryImpl() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    @Override
    public User add(Connection connection, User user) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO user (username, password, created_by, role_id) VALUES (?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setDate(3, Date.valueOf(user.getCreatedBy()));
            statement.setInt(4, user.getRole().getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer id = generatedKeys.getInt(1);
                    user.setId(id);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
        return user;
    }

    @Override
    public User getByName(Connection connection, String username) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT u.id, u.username, u.password, u.created_by, r.name AS role_name FROM user u " +
                                "JOIN role r ON u.role_id = r.id WHERE u.username = ? "
                )
        ) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                }
            }
            return null;
        }
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT u.username, u.password, u.created_by, r.name AS role_name " +
                                "FROM user u " +
                                "JOIN role r ON u.role_id = r.id"
                )
        ) {
            List<User> users = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = getUser(rs);
                    users.add(user);
                }
                return users;
            }
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        String username = rs.getString("username");

        String password = rs.getString("password");

        Date createdByDate = rs.getDate("created_by");
        LocalDate createdBy = createdByDate.toLocalDate();

        Role role = new Role();
        String roleName = rs.getString("role_name");
        role.setName(roleName);

        user.setUsername(username);
        user.setPassword(password);
        user.setCreatedBy(createdBy);
        user.setRole(role);
        return user;
    }

}
