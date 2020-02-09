package com.gmail.supersonicleader.app.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.app.repository.RoleRepository;
import com.gmail.supersonicleader.app.repository.model.Role;

public class RoleRepositoryImpl extends GeneralRepositoryImpl<Role> implements RoleRepository {

    private static RoleRepository instance;

    private RoleRepositoryImpl() {
    }

    public static RoleRepository getInstance() {
        if (instance == null) {
            instance = new RoleRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Role add(Connection connection, Role role) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO role (name, description) VALUES (?,?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, role.getName());
            statement.setString(2, role.getDescription());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating role failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Integer id = generatedKeys.getInt(1);
                    role.setId(id);
                } else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
            return role;
        }
    }

    @Override
    public List<Role> findAll(Connection connection) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name, description FROM role "
                )
        ) {
            List<Role> roles = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Role role = getRole(rs);
                    roles.add(role);
                }
                return roles;
            }
        }
    }

    @Override
    public Role getByName(Connection connection, String name) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT id, name, description FROM role WHERE name = ?"
                )
        ) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Role role = getRole(rs);
                    return role;
                }
            }
            return null;
        }
    }

    private Role getRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        Integer id = rs.getInt("id");

        String name = rs.getString("name");

        String description = rs.getString("description");

        role.setId(id);
        role.setName(name);
        role.setDescription(description);
        return role;
    }

}
